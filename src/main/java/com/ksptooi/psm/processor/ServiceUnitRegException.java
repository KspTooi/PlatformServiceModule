package com.ksptooi.psm.processor;

public class ServiceUnitRegException extends Exception{

    public ServiceUnitRegException() {
        super();
    }

    public ServiceUnitRegException(String message) {
        super(message);
    }

    public ServiceUnitRegException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnitRegException(Throwable cause) {
        super(cause);
    }

    protected ServiceUnitRegException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
