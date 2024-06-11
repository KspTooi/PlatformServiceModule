package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.entity.ActivatedSrvUnit;
import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class ServiceUnitRegisterEvent extends AbstractServiceUnitEvent {

    private final ActivatedSrvUnit proc;

    public ServiceUnitRegisterEvent(ActivatedSrvUnit proc){
        this.proc = proc;
    }

    @Override
    public PSMShell getUserShell() {
        return null;
    }
}
