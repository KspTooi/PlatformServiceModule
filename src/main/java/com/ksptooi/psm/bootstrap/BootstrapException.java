package com.ksptooi.psm.bootstrap;

public class BootstrapException extends Exception {
    public BootstrapException() {
        super();
    }

    public BootstrapException(String message) {
        super(message);
    }

    public BootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public BootstrapException(Throwable cause) {
        super(cause);
    }

    protected BootstrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
