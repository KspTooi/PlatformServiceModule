package com.ksptooi.psm.processor;

public class ServiceDefinitionException extends Exception{

    public ServiceDefinitionException() {
        super();
    }

    public ServiceDefinitionException(String message) {
        super(message);
    }

    public ServiceDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceDefinitionException(Throwable cause) {
        super(cause);
    }

    protected ServiceDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
