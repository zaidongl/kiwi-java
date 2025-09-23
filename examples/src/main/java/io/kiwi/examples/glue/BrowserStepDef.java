package io.kiwi.examples.glue;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.kiwi.agents.gui.WebBrowserAgent;
import io.kiwi.context.ScenarioContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

public class BrowserStepDef {
    private static final Logger logger = LogManager.getLogger(BrowserStepDef.class);
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

    @Then("{string} sees {string} on {string}")
    public void seesOn(String agentName, String text, String elementName) {
        WebBrowserAgent agent = (WebBrowserAgent) this.scenarioContext.getAgent(agentName);
        String actualText = agent.readText(elementName);
        scenarioContext.write("Actual text: " + actualText);
        Assert.assertNotNull(actualText);
        Assert.assertEquals(text, actualText);
    }
}
