package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.entity.ActiveProcessor;
import com.ksptooi.psm.processor.event.generic.AbstractProcEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class ProcRegisterEvent extends AbstractProcEvent {

    private final ActiveProcessor proc;

    public ProcRegisterEvent(ActiveProcessor proc){
        this.proc = proc;
    }

    @Override
    public PSMShell getUserShell() {
        return null;
    }
}
