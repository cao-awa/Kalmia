package com.github.kusa233.kalmia.entrypoint.exception;

public class KalmiaEntrypointStageFailedException extends RuntimeException {
    public final String stage;
    public final Throwable cause;

    public KalmiaEntrypointStageFailedException(String stage, Throwable cause){
        this.stage = stage;
        this.cause = cause;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
