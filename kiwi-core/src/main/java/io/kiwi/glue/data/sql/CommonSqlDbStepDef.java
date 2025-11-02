package io.kiwi.glue.data.sql;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.kiwi.agents.common.AgentsManager;
import io.kiwi.agents.data.sql.SqlDbAgent;
import io.kiwi.context.ScenarioContext;
import io.kiwi.context.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonSqlDbStepDef {
    private static final Logger logger = LogManager.getLogger(CommonSqlDbStepDef.class);
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

    @Given("I have {string} connect to sql database")
    public void connectToDatabase(String sqlDbAgent) {
        SqlDbAgent agent = (SqlDbAgent) AgentsManager.getInstance().getAgent(sqlDbAgent);
        StepResult stepResult = agent.connect();
        if(stepResult.getStatus() == StepResult.Status.FAILED){
            throw new RuntimeException("Failed to connect to database: " + stepResult.getMessage());
        }
    }

    @When("{string} executes sql query {string} and store result as {string}")
    public void executeSqlQuery(String sqlDbAgent, String sqlQuery, String resultVar) {
        SqlDbAgent agent = (SqlDbAgent) AgentsManager.getInstance().getAgent(sqlDbAgent);
        StepResult stepResult = agent.executeQuery(sqlQuery);
        scenarioContext.setVariable(resultVar, stepResult);
    }

}
