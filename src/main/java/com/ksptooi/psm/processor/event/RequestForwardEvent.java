package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.shell.ShellUser;
import lombok.Getter;

@Getter
public class RequestForwardEvent extends CancellableEvent{

    private final ShellUser user;
    private final ProcRequest request;

    public RequestForwardEvent(ShellUser user, ProcRequest request){
        this.user = user;
        this.request = request;
    }

}
