package com.ksptooi.asf.core.processor;

import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.Processor;

import java.util.HashMap;

public interface ProcessorDispatcher {


    public void register(HashMap<String,Processor> procMap);

    //注册命令处理器
    public boolean register(String procName, Processor proc);

    //发布命令
    public void publish(CliCommand command);

    //获取命令独占调度
    public void getExclusive(Processor proc);

    //取消命令独占
    public void removeExclusive();

}
