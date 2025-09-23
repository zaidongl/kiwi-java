package io.kiwi.context;

public class StepResult {
    public enum Status {
        PASSED,
        FAILED,
        SKIPPED
    }

    private Status status;
    private String message;
    private Object data;

    public StepResult(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public String getValue(){
        if(data != null){
            return data.toString();
        }
        return null;
    }
}
