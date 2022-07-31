package com.ksptooi.wphub.core.executor.dispatch;

import com.ksptooi.wphub.core.entities.PreparedCommand;

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
