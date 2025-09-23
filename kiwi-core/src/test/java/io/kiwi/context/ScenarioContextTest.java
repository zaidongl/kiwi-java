package io.kiwi.context;

import io.cucumber.java.Scenario;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class ScenarioContextTest {
    @Test
    public void testSetGetVariable() {
        Scenario mockScenario = mock(Scenario.class);
        ScenarioContext context = new ScenarioContext(mockScenario);

        context.write("Test log message");
        context.setVariable("key1", new StepResult(StepResult.Status.PASSED, "Test message", "value1"));
        StepResult retrieved = context.getVariable("key1");
        assert retrieved != null;
        assert "value1".equals(retrieved.getValue());
    }
}
