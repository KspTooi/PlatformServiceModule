package com.ksptooi.asf.core.processor.dispatcher;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.Processor;
import com.ksptooi.asf.core.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CmdProcessDispatcher implements ProcessorDispatcher {

    private final Logger logger = LoggerFactory.getLogger(CmdProcessDispatcher.class);

    //private final List<Listener> listenerList = new ArrayList<>();

    private final HashMap<String, Processor> listenerMap = new HashMap<>();

    @Inject
    private CommandService service;

    @Override
    public boolean addListener(String listenerName, Processor listener) {

        //注入内部组件
        ServiceFrame.injector.injectMembers(listener);
        logger.info("已注册命令执行器:"+listenerName);
        //this.listenerList.add(listener);
        this.listenerMap.put(listenerName,listener);
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
        Processor listener = this.listenerMap.get(commandByName.getExecutorName());


        if(listener == null){
            logger.info("命令推送失败,该命令无执行器.");
            return;
        }

        //向执行器发布命令
        listener.onCommand(inVo,commandByName);
    }


    @Override
    public void getExclusive(Processor listener) {

    }

    @Override
    public void removeExclusive() {

    }

}
