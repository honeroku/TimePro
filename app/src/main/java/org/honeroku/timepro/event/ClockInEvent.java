package org.honeroku.timepro.event;

public class ClockInEvent extends Event {

    public static ClockInEvent createAsSuccess() {
        ClockInEvent event = new ClockInEvent();
        event.setResult(Result.SUCCESS);
        return event;
    }

    public static ClockInEvent createAsFailed(String message) {
        ClockInEvent event = new ClockInEvent();
        event.setResult(Result.FAILED);
        event.setMessage(message);
        return event;
    }

}
