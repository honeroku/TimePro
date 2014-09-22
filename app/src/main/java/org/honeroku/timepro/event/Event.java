package org.honeroku.timepro.event;

import org.honeroku.timepro.util.EventBus;

public class Event {

    private Result result;
    private String message;

    public void publish() {
        EventBus.getInstance().postOnMain(this);
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.result == Result.SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return message != null && !message.isEmpty();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static enum Result {
        SUCCESS, FAILED
    }
}
