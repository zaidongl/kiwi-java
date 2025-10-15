package io.kiwi.examples.glue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.kiwi.agents.common.AgentsManager;
import io.kiwi.agents.mock.WireMockAgent;
import io.kiwi.context.ScenarioContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestStepDef {
    private static final Logger logger = LogManager.getLogger(RestStepDef.class);
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

    @Given("{string} is running")
    public void wireMockAgentIsRunning(String agentName) {
        WireMockAgent agent = (WireMockAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.start();
        this.scenarioContext.write("WireMock agent '" + agentName + "' is running.");
    }

    @Then("stop WireMock agent {string}")
    public void stopWireMockAgent(String agentName) {
        WireMockAgent agent = (WireMockAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.stop();
        this.scenarioContext.write("WireMock agent '" + agentName + "' has been stopped.");
    }
}
