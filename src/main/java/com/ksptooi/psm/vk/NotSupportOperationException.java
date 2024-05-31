package com.ksptooi.psm.vk;

public class NotSupportOperationException extends RuntimeException{

    public NotSupportOperationException() {
        super();
    }

    public NotSupportOperationException(String message) {
        super(message);
    }

    public NotSupportOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportOperationException(Throwable cause) {
        super(cause);
    }

    protected NotSupportOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
