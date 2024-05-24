package com.ksptooi.uac.core.processor;

import com.google.inject.Inject;
import com.ksptooi.uac.ApplicationOld;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class CmdProcessDispatcher implements ProcessorDispatcher {

    private final Logger logger = LoggerFactory.getLogger(CmdProcessDispatcher.class);

    //private final List<Listener> listenerList = new ArrayList<>();

    private final HashMap<String, Processor> processorMap = new HashMap<>();

    //current activity processor(Background Processor)
    private Processor activity = null;

    @Inject
    private CommandService service;

    @Override
    public void register(Map<String, Processor> procMap) {
        for(Map.Entry<String,Processor> item:procMap.entrySet()){
            this.register(item.getKey(),item.getValue());
        }
    }

    @Override
    public boolean register(String listenerName, Processor listener) {

        if(processorMap.get(listenerName) != null){
            logger.warn("处理器注册失败,该处理器名称已被占用:{}",listenerName);
            return false;
        }

        //注入内部组件
        ApplicationOld.injector.injectMembers(listener);
        logger.info("已注册命令处理器:"+listenerName);
        //this.listenerList.add(listener);
        this.processorMap.put(listenerName,listener);
        listener.onInit();
        return true;
    }

    @Override
    public boolean remove(String procName) {
        Processor remove = this.processorMap.remove(procName);
        return remove!=null;
    }


    @Override
    public void publish(CliCommand inVo) {

        //查询出该命令对应的执行器
        Command commandByName = service.getCommandByName(inVo.getName());

        if(commandByName == null){
            logger.info("{} 不是一个UAC指令.",inVo.getName());
            return;
        }

        //查找已注册的执行器
        Processor proc = this.processorMap.get(commandByName.getExecutorName());


        if(proc == null){
            logger.info("{} 没有对应的指令处理器.",inVo.getName());
            return;
        }

        //向执行器发布命令
        proc.onCommand(inVo,commandByName);
    }


    @Override
    public void getActivity(Processor listener) {
        this.activity = listener;
    }

    @Override
    public void removeActivity() {
        this.activity = null;
    }

    protected void setActivity(Processor in){
        this.activity = in;
    }

    @Override
    public List<String> getRegisteredProcessor() {
        Set<String> strings = this.processorMap.keySet();
        return new ArrayList<>(strings);
    }

    public Map<String, Processor> getProcessorMap() {
        return processorMap;
    }
}
