package com.github.cao.awa.kalmia.env.security.exception;

public class PreShareKeyNotFoundException extends Exception {
    public PreShareKeyNotFoundException() {
        super();
    }

    public PreShareKeyNotFoundException(String message) {
        super(message);
    }

    public PreShareKeyNotFoundException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public PreShareKeyNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PreShareKeyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
