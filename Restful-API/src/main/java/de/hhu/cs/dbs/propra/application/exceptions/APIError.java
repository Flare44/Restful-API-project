package de.hhu.cs.dbs.propra.application.exceptions;

import org.glassfish.jersey.internal.util.ExceptionUtils;

public class APIError {
    private final String message;
    private final String stackTrace;

    public APIError(String message, Throwable throwable) {
        this.message = message;
        this.stackTrace = ExceptionUtils.exceptionStackTraceAsString(throwable);
    }

    public APIError(String message) {
        this.message = message;
        this.stackTrace = null;
    }

    public APIError(Throwable throwable) {
        this.message = throwable.getMessage();
        this.stackTrace = ExceptionUtils.exceptionStackTraceAsString(throwable);
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
