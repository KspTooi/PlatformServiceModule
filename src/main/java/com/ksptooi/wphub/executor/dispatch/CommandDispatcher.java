package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.ApplicationRunner;
import com.ksptooi.wphub.entities.PreparedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandDispatcher implements CommandScheduler {

    private final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);

    private final List<Listener> listenerList = new ArrayList<>();

    @Override
    public boolean addListener(String listenerName, Listener listener) {
        listener.setName(listenerName);

        //注入内部组件
        ApplicationRunner.injector.injectMembers(listener);

        logger.info("已注册命令执行器:"+listenerName);

        this.listenerList.add(listener);
        return true;
    }


    @Override
    public void publish(PreparedCommand command) {

        //查询出该命令对应的执行器


        //查找已注册的执行器


        //向执行器发布命令

        for(Listener item:this.listenerList){
            item.onCommand(command);
        }

    }

}
