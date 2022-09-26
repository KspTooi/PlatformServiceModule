package com.ksptooi.uac.core.processor;

import com.ksptooi.uac.core.entities.CliCommand;

import java.util.List;
import java.util.Map;

public interface ProcessorDispatcher {


    public void register(Map<String,Processor> procMap);

    //注册命令处理器
    public boolean register(String procName, Processor proc);

    //移除已经注册的命令处理器
    public boolean remove(String procName);

    //发布命令
    public void publish(CliCommand command);

    //获取命令独占调度
    public void getActivity(Processor proc);

    //取消命令独占
    public void removeActivity();

    public List<String> getRegisteredProcessor();

}
