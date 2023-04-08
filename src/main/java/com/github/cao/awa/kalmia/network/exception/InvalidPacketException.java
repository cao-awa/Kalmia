package com.github.cao.awa.kalmia.network.exception;

public class InvalidPacketException extends RuntimeException {
    public InvalidPacketException() {
        super();
    }

    public InvalidPacketException(String message) {
        super(message);
    }

    public InvalidPacketException(String message, Throwable cause) {
        super(message,
              cause
        );
    }

    public InvalidPacketException(Throwable cause) {
        super(cause);
    }

    protected InvalidPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace
        );
    }
}
