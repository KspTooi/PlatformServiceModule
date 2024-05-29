package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.entity.ActiveProcessor;
import lombok.Getter;

@Getter
public class ProcRegisterEvent extends CancellableEvent{

    private final ActiveProcessor proc;

    public ProcRegisterEvent(ActiveProcessor proc){
        this.proc = proc;
    }

}
