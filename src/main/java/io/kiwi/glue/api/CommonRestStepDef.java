package io.kiwi.glue.api;

import io.kiwi.agents.common.AgentsManager;
import io.kiwi.agents.api.RestAgent;
import io.kiwi.context.ScenarioContext;
import io.kiwi.context.StepResult;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


public class CommonRestStepDef {
    private static final Logger logger = LogManager.getLogger(CommonRestStepDef.class);
    ScenarioContext scenarioContext = null;

    @Before
    public void beforeScenario(Scenario scenario) {
        this.scenarioContext = new ScenarioContext(scenario);
        logger.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void tearDown() {
        logger.info("Finished Scenario: {}", this.scenarioContext.getScenario().getName());
        this.scenarioContext = null;
    }

    @Given("I have {string} {string}")
    public void i_have_a_restful_service_endpoint(String variableName, String url) {
        StepResult stepResult = new StepResult(StepResult.Status.PASSED, "", url);
        scenarioContext.setVariable(variableName, stepResult);
    }

    @Given("I have headers {string} as below")
    public void iHaveAsBelow(String variableName, DataTable dataTable) {
        Map<String, String> headers = dataTable.asMap(String.class, String.class);
        StepResult stepResult = new StepResult(StepResult.Status.PASSED, "", headers);
        scenarioContext.setVariable(variableName, stepResult);
    }


    @And("I have payload {string} from json file {string}")
    public void iHavePayloadFromJsonFile(String payloadVar, String jsonFilePath) throws IOException {
        //read the json file and convert to string

        String payload = Files.readString(Path.of(jsonFilePath));
        StepResult stepResult = new StepResult(StepResult.Status.PASSED, "", payload);
        scenarioContext.setVariable(payloadVar, stepResult);
        scenarioContext.write("Payload from file " + jsonFilePath + ": " + payload);
    }

    @When("{string} send a {string} request to endpoint {string} with {string}, {string} and {string}")
    public void iSendAPOSTRequestToEndpoint(String agentName, String method, String url, String headersVar,
                                            String paramsVar, String payloadVar){
        RestAgent restAgent = (RestAgent) AgentsManager.getInstance().getAgent(agentName);
        Map<String, String> headers = null;
        if(headersVar != null && !headersVar.isEmpty()){
            StepResult headersStepResult = scenarioContext.getVariable(headersVar);
            headers = (Map<String, String>) headersStepResult.getData();
        }

        Map<String, String> params = null;
        if(paramsVar != null && !paramsVar.isEmpty()){
            StepResult paramsStepResult = scenarioContext.getVariable(paramsVar);
            params = (Map<String, String>) paramsStepResult.getData();
        }

        StepResult payloadStepResult = scenarioContext.getVariable(payloadVar);
        String payload = payloadStepResult.getData().toString();
        StepResult stepResult = restAgent.sendRequest(method, url, headers, params, payload);
        scenarioContext.setLastStepResult(stepResult);
    }

    @Then("Get response as {string}")
    public void getResponseAs(String responseVarName) {
        scenarioContext.setVariable(responseVarName, scenarioContext.getLastStepResult());
    }

    @Then("The {string} status code should be {int}")
    public void theStatusCodeShouldBe(String responseVarName, int expectedStatusCode) {
        Response response = (Response) scenarioContext.getVariable(responseVarName).getData();
        int actualStatusCode = response.getStatusCode();
//        scenarioContext.write("Expected status code: " + expectedStatusCode + ", Actual status code: " + actualStatusCode);
        if(actualStatusCode != expectedStatusCode){
            throw new AssertionError("Expected status code: " + expectedStatusCode + ", but got: " + actualStatusCode);
        }
    }

    @Then("The {string} body should contain {string}")
    public void theBodyShouldContain(String responseBodyVar, String expectedSubContent) {
        Response response = (Response) scenarioContext.getVariable(responseBodyVar).getData();
        String responseBody = response.getBody().asString();
        scenarioContext.write("Response body: " + responseBody);
        if(!responseBody.contains(expectedSubContent)){
            throw new AssertionError("Response body does not contain expected content: " + expectedSubContent);
        }
    }
}
