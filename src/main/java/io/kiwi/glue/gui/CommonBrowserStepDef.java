package io.kiwi.glue.gui;

import io.cucumber.java.en.When;
import io.kiwi.agents.common.AgentsManager;
import io.kiwi.agents.gui.WebBrowserAgent;
import io.kiwi.context.ScenarioContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonBrowserStepDef {
    private static final Logger logger = LogManager.getLogger(CommonBrowserStepDef.class);
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

    @Given("{string} open page {string}")
    public void iOpenBrowserWithUrl(String agentName, String url) {
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.navigateTo(url);
    }


    @Then("{string} should see UI element {string}")
    public void shouldSeeButton(String agentName, String elementName) {
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.waitForSelector(elementName);
    }

    @When("{string} click UI element {string}")
    public void clickElement(String agentName, String elementName) {
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.click(elementName);
    }

    @When("{string} input {string} into UI element {string}")
    public void inputIntoElement(String agentName, String text, String elementName) {
        String processedText = scenarioContext.evaluateVariable(text);
         logger.info("Processed text after variable replacement: {}", processedText);
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.type(elementName, processedText);
    }

    @When("{string} input ciphertext {string} into text box {string}")
    public void inputIntoPasswordTextbox(String agentName, String ciphertext, String elementName) throws Exception {
        //ciphertext could be passed from variable, need to evaluate first
        String processedCiphertext = scenarioContext.evaluateVariable(ciphertext);
        String plainText = scenarioContext.processCiphertext(processedCiphertext);
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.type(elementName, plainText);
    }

    @When("{string} press {string} key on UI element {string}")
    public void pressKey(String agentName, String key, String elementName) {
        WebBrowserAgent agent = (WebBrowserAgent) AgentsManager.getInstance().getAgent(agentName);
        agent.press(elementName, key);
    }
}
