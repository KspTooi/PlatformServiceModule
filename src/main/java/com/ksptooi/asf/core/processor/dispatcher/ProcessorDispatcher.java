package com.ksptooi.asf.core.processor.dispatcher;

import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.Processor;

public interface ProcessorDispatcher {

    //添加命令监听器
    public boolean addListener(String listenerName, Processor listener);

    //发布命令
    public void publish(CliCommand command);

    //获取命令独占调度
    public void getExclusive(Processor listener);

    //取消命令独占
    public void removeExclusive();

}
