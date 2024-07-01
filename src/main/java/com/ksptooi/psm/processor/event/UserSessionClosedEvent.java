package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class UserSessionClosedEvent extends AbstractServiceUnitEvent {

    private final String userName;
    private final String sessionId;


    public UserSessionClosedEvent(String userName,String sessionId){
        this.userName = userName;
        this.sessionId = sessionId;
    }

    @Override
    public PSMShell getUserShell() {
        return null;
    }

}
