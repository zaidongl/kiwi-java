package io.kiwi.context;

import io.cucumber.java.Scenario;
import io.kiwi.agents.common.Agent;
import io.kiwi.agents.common.AgentsManager;
import io.kiwi.security.crypto.AESCipher;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private final Scenario scenario;
    private final Map<String, StepResult> scenarioVariables;
    private StepResult lastStepResult;
    private Agent currentAgent;

    public ScenarioContext(Scenario scenario) {
        this.scenario = scenario;
        this.scenarioVariables = new HashMap<>();
        this.lastStepResult = null;
    }

    public void write(String text) {
        scenario.log(text);
    }

    public void setVariable(String key, StepResult value) {
        scenarioVariables.put(key, value);
    }

    public StepResult getVariable(String key) {
        return scenarioVariables.get(key);
    }

    public boolean hasVariable(String key) {
        return scenarioVariables.containsKey(key);
    }

    public StepResult getLastStepResult() {
        return lastStepResult;
    }

    public void setLastStepResult(StepResult lastStepResult) {
        this.lastStepResult = lastStepResult;
    }

    public Scenario getScenario(){
        return this.scenario;
    }

    /**
     * Evaluate variables in the input string and replace them with their corresponding values from scenarioVariables.
     * @param input the input string possibly containing variables in the format @variableName
     * @return the input string with variables replaced by their values
     */
    public String evaluateVariable(String input) {
        return VariableEvaluator.evaluate(input, this);
    }

    public String processCiphertext(String ciphertext) throws Exception {
        return AESCipher.getInstance().decrypt(ciphertext);
    }

    public Agent getAgent(String agentName) {
        Agent agent = AgentsManager.getInstance().getAgent(agentName);
        if (agent == null) {
            throw new RuntimeException("Agent " + agentName + " not found.");
        }
        currentAgent = agent;

        return agent;
    }

    public Agent getCurrentAgent() {
        return currentAgent;
    }
}
