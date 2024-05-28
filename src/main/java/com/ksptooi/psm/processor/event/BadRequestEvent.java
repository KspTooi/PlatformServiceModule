package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.ProcRequest;
import lombok.Getter;

@Getter
public class BadRequestEvent implements ProcEvent{

    private final ProcRequest request;

    private final String errorCode;

    private final Exception exception;

    public BadRequestEvent(ProcRequest request,String errorCode){
        this.request = request;
        this.errorCode = errorCode;
        this.exception = null;
    }

    public BadRequestEvent(ProcRequest request,String errorCode,Exception ex){
        this.request = request;
        this.errorCode = errorCode;
        this.exception = ex;
    }

    @Override
    public boolean isIntercepted() {
        return false;
    }

    public static final String ERR_UNKNOWN_HANDLER = "PSM:STM:E0";

    public static final String ERR_HANDLER_TYPE_INCONSISTENT = "PSM:STM:E1";

    public static final String ERR_CANNOT_ASSIGN_HANDLER = "PSM:STM:E2";

    public static final String ERR_INVOKE_EXCEPTION = "PSM:STM:E3";

}
