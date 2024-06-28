package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BadRequestEvent extends AbstractServiceUnitEvent {

    private final ShellRequest request;

    private final String errorCode;

    private final Exception exception;

    @Setter
    private String responseText;

    public BadRequestEvent(ShellRequest request, String errorCode){
        this.request = request;
        this.errorCode = errorCode;
        this.exception = null;
    }

    public BadRequestEvent(ShellRequest request, String errorCode, Exception ex){
        this.request = request;
        this.errorCode = errorCode;
        this.exception = ex;
    }


    @Override
    public PSMShell getUserShell() {
        return request.getShell();
    }

    @Override
    public boolean isIntercepted() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    public static final String ERR_UNKNOWN_HANDLER = "PSM:STM:E0";

    public static final String ERR_HANDLER_TYPE_INCONSISTENT = "PSM:STM:E1";

    public static final String ERR_CANNOT_ASSIGN_HANDLER = "PSM:STM:E2";

    public static final String ERR_INVOKE_EXCEPTION = "PSM:STM:E3";

    public static final String ERR_ASSEMBLY_ARGUMENT = "PSM:STM:E6";

}
