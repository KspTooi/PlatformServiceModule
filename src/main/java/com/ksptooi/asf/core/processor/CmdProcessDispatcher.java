package com.ksptooi.asf.core.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CmdProcessDispatcher implements ProcessorDispatcher {

    private final Logger logger = LoggerFactory.getLogger(CmdProcessDispatcher.class);

    //private final List<Listener> listenerList = new ArrayList<>();

    private final HashMap<String, Processor> processorMap = new HashMap<>();

    @Inject
    private CommandService service;

    @Override
    public boolean register(String listenerName, Processor listener) {

        //注入内部组件
        ServiceFrame.injector.injectMembers(listener);
        logger.info("已注册命令处理器:"+listenerName);
        //this.listenerList.add(listener);
        this.processorMap.put(listenerName,listener);
        listener.onInit();
        return true;
    }

    @Override
    public void publish(CliCommand inVo) {

        //查询出该命令对应的执行器
        Command commandByName = service.getCommandByName(inVo.getName());

        if(commandByName == null){
            logger.info("命令推送失败,数据库无记录.");
            return;
        }

        //查找已注册的执行器
        Processor proc = this.processorMap.get(commandByName.getExecutorName());


        if(proc == null){
            logger.info("命令推送失败,该命令无处理器.");
            return;
        }

        //向执行器发布命令
        proc.onCommand(inVo,commandByName);
    }


    @Override
    public void getExclusive(Processor listener) {

    }

    @Override
    public void removeExclusive() {

    }

    public HashMap<String, Processor> getProcessorMap() {
        return processorMap;
    }
}
