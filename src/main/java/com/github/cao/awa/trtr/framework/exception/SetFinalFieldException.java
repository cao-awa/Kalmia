package com.github.cao.awa.trtr.framework.exception;

public class SetFinalFieldException extends InvertOfControlException {
    public SetFinalFieldException() {
        super();
    }

    public SetFinalFieldException(String message) {
        super(message);
    }

    public SetFinalFieldException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public SetFinalFieldException(Throwable cause) {
        super(cause);
    }

    protected SetFinalFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
