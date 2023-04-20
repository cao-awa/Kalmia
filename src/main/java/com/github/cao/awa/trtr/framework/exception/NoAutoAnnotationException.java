package com.github.cao.awa.trtr.framework.exception;

public class NoAutoAnnotationException extends InvertOfControlException {
    public NoAutoAnnotationException(String message) {
        super(message);
    }

    public NoAutoAnnotationException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public NoAutoAnnotationException(Throwable cause) {
        super(cause);
    }

    protected NoAutoAnnotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }

    public NoAutoAnnotationException() {
        super();
    }
}
