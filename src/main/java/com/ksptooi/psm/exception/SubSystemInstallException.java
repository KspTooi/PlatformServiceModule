package com.ksptooi.psm.exception;

public class SubSystemInstallException extends RuntimeException{

    public SubSystemInstallException() {
        super();
    }

    public SubSystemInstallException(String message) {
        super(message);
    }

    public SubSystemInstallException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubSystemInstallException(Throwable cause) {
        super(cause);
    }

    protected SubSystemInstallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
