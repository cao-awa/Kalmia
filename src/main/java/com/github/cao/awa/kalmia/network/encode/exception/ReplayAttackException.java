package com.github.cao.awa.kalmia.network.encode.exception;

public class ReplayAttackException extends RuntimeException {
    public ReplayAttackException() {
        super();
    }

    public ReplayAttackException(String message) {
        super(message);
    }

    public ReplayAttackException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public ReplayAttackException(Throwable cause) {
        super(cause);
    }

    protected ReplayAttackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
