package io.kiwi.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VariableEvaluatorTest {
    @Test
    public void testEvaluate_ParseVar() {
        String input = "@name";
        ScenarioContext context = new ScenarioContext(null);
        context.setVariable("@name", new StepResult(StepResult.Status.PASSED, "Name variable", "Alice"));
        context.setVariable("@day", new StepResult(StepResult.Status.PASSED, "Day variable", "Monday"));

        String result = VariableEvaluator.evaluate(input, context);
        Assertions.assertEquals("Alice", result);
    }

    @Test
    public void testEvaluate_EscapeVarPrefix() {
        String input = "\\@name.";
        ScenarioContext context = new ScenarioContext(null);
        context.setVariable("@name", new StepResult(StepResult.Status.PASSED, "Name variable", "Alice"));
        context.setVariable("@day", new StepResult(StepResult.Status.PASSED, "Day variable", "Monday"));

        String result = VariableEvaluator.evaluate(input, context);
        Assertions.assertEquals("@name.", result);
    }

    @Test
    public void testEvaluate_EscapeEscapeCharacter() {
        String input = "\\\\@name.";
        ScenarioContext context = new ScenarioContext(null);
        context.setVariable("@name", new StepResult(StepResult.Status.PASSED, "Name variable", "Alice"));
        context.setVariable("@day", new StepResult(StepResult.Status.PASSED, "Day variable", "Monday"));

        String result = VariableEvaluator.evaluate(input, context);
        Assertions.assertEquals("\\@name.", result);
    }
}
