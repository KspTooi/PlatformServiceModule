package com.ksptooi.asf.core.executor;

import com.ksptooi.asf.core.entities.PreparedCommand;

public class CommandExclusiveWrapper extends CommandDispatcher{

    @Override
    public void publish(PreparedCommand inVo) {
        super.publish(inVo);
    }

    @Override
    public void getExclusive(Listener listener) {
        super.getExclusive(listener);
    }

    @Override
    public void removeExclusive() {
        super.removeExclusive();
    }

}
