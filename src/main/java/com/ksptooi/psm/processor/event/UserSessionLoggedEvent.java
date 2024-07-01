package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class UserSessionLoggedEvent extends AbstractServiceUnitEvent {

    private final PSMShell shell;
    private final String userName;
    private final String sessionId;


    public UserSessionLoggedEvent(PSMShell shell,String userName,String sessionId){
        this.shell = shell;
        this.userName = userName;
        this.sessionId = sessionId;
    }

    @Override
    public PSMShell getUserShell() {
        return shell;
    }

}
