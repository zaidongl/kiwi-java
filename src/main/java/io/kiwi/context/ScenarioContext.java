package io.kiwi.context;

import io.cucumber.java.Scenario;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private final Scenario scenario;
    private final Map<String, StepResult> scenarioVariables;
    private StepResult lastStepResult;

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
}
