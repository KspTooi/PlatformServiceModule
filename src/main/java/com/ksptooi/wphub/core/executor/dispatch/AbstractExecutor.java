package com.ksptooi.wphub.core.executor.dispatch;

import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.entities.PreparedCommand;

public abstract class AbstractExecutor implements Listener{

    @Override
    public void onInit() {

    }

    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

}
