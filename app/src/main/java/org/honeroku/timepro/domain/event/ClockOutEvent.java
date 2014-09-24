package org.honeroku.timepro.domain.event;

public class ClockOutEvent extends Event {

    public static ClockOutEvent createAsSuccess() {
        ClockOutEvent event = new ClockOutEvent();
        event.setResult(Result.SUCCESS);
        return event;
    }

    public static ClockOutEvent createAsFailed(String message) {
        ClockOutEvent event = new ClockOutEvent();
        event.setResult(Result.FAILED);
        event.setMessage(message);
        return event;
    }

}
