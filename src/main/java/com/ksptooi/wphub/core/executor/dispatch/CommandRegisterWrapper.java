package com.ksptooi.wphub.core.executor.dispatch;

import com.google.inject.Inject;
import com.ksptooi.wphub.commons.IdWorker;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CommandRegisterWrapper extends CommandExclusiveWrapper{

    private final Logger logger = LoggerFactory.getLogger(CommandRegisterWrapper.class);

    @Inject
    private CommandService service;


    @Override
    public boolean addListener(String listenerName, Listener listener) {

        logger.info("检查执行器基本指令组:"+listenerName);

        String[] defaultCmd = listener.defaultCommand();

        for(String item : defaultCmd){

            if(!service.hasCommand(item)){

                logger.info("新增基本指令:" + item);
                Command insert = new Command();
                insert.setCmdId(new IdWorker().nextId());
                insert.setName(item);
                insert.setExecutorName(listenerName);
                insert.setCreateTime(new Date());
                insert.setDescription("default");
                service.insert(insert);
            }

        }

        logger.info("执行器检查通过:"+listenerName);

        return super.addListener(listenerName, listener);
    }

}
