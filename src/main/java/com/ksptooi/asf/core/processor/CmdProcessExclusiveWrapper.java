package com.ksptooi.asf.core.processor;

import com.ksptooi.asf.core.entities.CliCommand;

public class CmdProcessExclusiveWrapper extends CmdProcessDispatcher {

    @Override
    public void publish(CliCommand inVo) {
        super.publish(inVo);
    }

    @Override
    public void getExclusive(Processor listener) {
        super.getExclusive(listener);
    }

    @Override
    public void removeExclusive() {
        super.removeExclusive();
    }

}
