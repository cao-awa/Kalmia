package com.github.cao.awa.kalmia.keypair.exception;

public class UnableToDecodeException extends RuntimeException {
    public UnableToDecodeException() {
        super();
    }

    public UnableToDecodeException(String message) {
        super(message);
    }

    public UnableToDecodeException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public UnableToDecodeException(Throwable cause) {
        super(cause);
    }

    protected UnableToDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
