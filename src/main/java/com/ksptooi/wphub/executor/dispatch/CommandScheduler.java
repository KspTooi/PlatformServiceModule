package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.entities.PreparedCommand;

public interface CommandScheduler {

    //添加命令监听器
    public boolean addListener(String listenerName, Listener listener);

    //发布命令
    public void publish(PreparedCommand command);

}
