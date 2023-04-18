package com.github.cao.awa.kalmia.chat.session.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException() {
        super();
    }

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public SessionNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SessionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
