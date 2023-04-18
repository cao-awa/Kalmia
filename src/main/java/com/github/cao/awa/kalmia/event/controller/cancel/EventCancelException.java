package com.github.cao.awa.kalmia.event.controller.cancel;

public class EventCancelException extends RuntimeException {
    public EventCancelException() {
        super();
    }

    public EventCancelException(String message) {
        super(message);
    }
}
