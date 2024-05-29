package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.shell.ShellInstance;
import lombok.Getter;

@Getter
public class RequestForwardEvent extends CancellableEvent{

    private final ShellInstance user;
    private final ProcRequest request;

    public RequestForwardEvent(ShellInstance user, ProcRequest request){
        this.user = user;
        this.request = request;
    }

}
