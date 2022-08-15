package com.ksptooi.asf.core.processor;

import com.ksptooi.asf.core.entities.CliCommand;

public class CmdProcessExclusiveWrapper extends CmdProcessDispatcher {

    @Override
    public void publish(CliCommand inVo) {
        super.publish(inVo);
    }

    @Override
    public void getActivity(Processor listener) {
        super.getActivity(listener);
    }

    @Override
    public void removeActivity() {
        super.removeActivity();
    }

}
