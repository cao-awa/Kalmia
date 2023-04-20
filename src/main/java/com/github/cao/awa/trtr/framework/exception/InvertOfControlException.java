package com.github.cao.awa.trtr.framework.exception;

public abstract class InvertOfControlException extends RuntimeException {
    public InvertOfControlException(String message) {
        super(message);
    }

    public InvertOfControlException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public InvertOfControlException(Throwable cause) {
        super(cause);
    }

    protected InvertOfControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }

    public InvertOfControlException() {
        super();
    }
}
