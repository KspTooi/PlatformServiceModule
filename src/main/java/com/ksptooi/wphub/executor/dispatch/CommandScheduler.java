package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.entities.PreparedCommand;

public interface CommandScheduler {

    public boolean addListener(String listenerName, Listener listener);

    public void publish(PreparedCommand command);

}
