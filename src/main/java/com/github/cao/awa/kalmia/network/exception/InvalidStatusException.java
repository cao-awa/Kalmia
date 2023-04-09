package com.github.cao.awa.kalmia.network.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super();
    }

    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public InvalidStatusException(Throwable cause) {
        super(cause);
    }

    protected InvalidStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
