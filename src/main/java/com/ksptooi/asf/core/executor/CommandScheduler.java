package com.ksptooi.asf.core.executor;

import com.ksptooi.asf.core.entities.PreparedCommand;

public interface CommandScheduler {

    //添加命令监听器
    public boolean addListener(String listenerName, Listener listener);

    //发布命令
    public void publish(PreparedCommand command);

    //获取命令独占调度
    public void getExclusive(Listener listener);

    //取消命令独占
    public void removeExclusive();

}
