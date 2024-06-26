package com.ksptooi.psm.processor;

public class AssemblingException extends Exception{
    public AssemblingException() {
        super();
    }

    public AssemblingException(String message) {
        super(message);
    }

    public AssemblingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssemblingException(Throwable cause) {
        super(cause);
    }

    protected AssemblingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
