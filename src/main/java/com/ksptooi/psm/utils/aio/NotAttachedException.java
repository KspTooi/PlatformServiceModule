package com.ksptooi.psm.utils.aio;

import java.io.IOException;

public class NotAttachedException extends IOException {

    public NotAttachedException() {
        super();
    }

    public NotAttachedException(String message) {
        super(message);
    }

    public NotAttachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAttachedException(Throwable cause) {
        super(cause);
    }
}
