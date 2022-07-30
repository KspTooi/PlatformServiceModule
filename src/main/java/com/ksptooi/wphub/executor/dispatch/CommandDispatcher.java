package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.entities.PreparedCommand;

public class CommandDispatcher implements CommandScheduler {


    @Override
    public boolean addListener(String listenerName, Listener listener) {
        return false;
    }

    @Override
    public void publish(PreparedCommand command) {

    }

}
