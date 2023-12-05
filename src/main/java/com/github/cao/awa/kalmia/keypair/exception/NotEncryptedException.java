package com.github.cao.awa.kalmia.keypair.exception;

public class NotEncryptedException extends RuntimeException {
    public NotEncryptedException() {
        super();
    }

    public NotEncryptedException(String message) {
        super(message);
    }

    public NotEncryptedException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public NotEncryptedException(Throwable cause) {
        super(cause);
    }

    protected NotEncryptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
