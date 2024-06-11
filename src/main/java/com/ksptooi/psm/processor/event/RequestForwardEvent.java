package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.processor.event.generic.AbstractProcEvent;
import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.shell.ShellInstance;
import lombok.Getter;

@Getter
public class RequestForwardEvent extends AbstractProcEvent {

    private final ShellInstance user;
    private final ShellRequest request;

    public RequestForwardEvent(ShellInstance user, ShellRequest request){
        this.user = user;
        this.request = request;
    }

    @Override
    public PSMShell getUserShell() {
        return null;
    }
}
