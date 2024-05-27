package com.ksptooi.psm.processor;

public class ProcDefineException extends Exception{

    public ProcDefineException() {
        super();
    }

    public ProcDefineException(String message) {
        super(message);
    }

    public ProcDefineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcDefineException(Throwable cause) {
        super(cause);
    }

    protected ProcDefineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
