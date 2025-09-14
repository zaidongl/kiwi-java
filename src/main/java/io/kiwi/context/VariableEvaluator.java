package io.kiwi.context;

public class VariableEvaluator {

    /**
     * Evaluates the input string by replacing variable name starting with '@' with actual values from the ScenarioContext.
     * e.g. @varName
     * To escape the '@' or '\' character, use '\',
     * e.g.
     * '\@varName' will be evaluated to string '@varName'     *
     * '\\' will be evaluated to string '\'
     * @param input   The input string containing placeholders.
     * @param context The ScenarioContext containing variables and last step result.
     * @return The evaluated string with variable replaced by actual values.
     */
    public static String evaluate(String input, ScenarioContext context) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        if(input.charAt(0) == '@' && context.hasVariable(input)){
            StepResult stepResult = context.getVariable(input);
            if(stepResult != null && stepResult.getData() != null){
                return stepResult.getData().toString();
            }
        }

        return processEscapeSequences(input);
    }

    private static String processEscapeSequences(String input) {
        StringBuilder result = new StringBuilder();
        boolean escapeNext = false;

        for (char c : input.toCharArray()) {
            if (escapeNext) {
                result.append(c);
                escapeNext = false;
            } else if (c == '\\') {
                escapeNext = true;
            } else {
                result.append(c);
            }
        }

        // If the last character was a backslash, append it
        if (escapeNext) {
            result.append('\\');
        }

        return result.toString();
    }
}
