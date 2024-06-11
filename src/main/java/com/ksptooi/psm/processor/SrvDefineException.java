package com.ksptooi.psm.processor;

public class SrvDefineException extends Exception{

    public SrvDefineException() {
        super();
    }

    public SrvDefineException(String message) {
        super(message);
    }

    public SrvDefineException(String message, Throwable cause) {
        super(message, cause);
    }

    public SrvDefineException(Throwable cause) {
        super(cause);
    }

    protected SrvDefineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
