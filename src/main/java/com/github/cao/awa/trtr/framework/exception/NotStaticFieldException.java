package com.github.cao.awa.trtr.framework.exception;

public class NotStaticFieldException extends InvertOfControlException {
    public NotStaticFieldException() {
        super();
    }

    public NotStaticFieldException(String message) {
        super(message);
    }

    public NotStaticFieldException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public NotStaticFieldException(Throwable cause) {
        super(cause);
    }

    protected NotStaticFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
