package com.ksptooi.wphub.extendstion.executor;

import com.google.inject.Inject;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.entities.PreparedCommand;
import com.ksptooi.wphub.core.executor.dispatch.Listener;
import com.ksptooi.wphub.extendstion.service.PackManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackManagerExecutor implements Listener {

    //执行器名称
    private final String executorName = "build-in-PackManagerExecutor";

    private final Logger logger = LoggerFactory.getLogger(PackManagerExecutor.class);


    @Inject
    private PackManagerService service;


    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

    @Override
    public void onInit() {
        logger.info("检查基本指令组");
    }



    @Override
    public void onCommand(PreparedCommand preparedCommand, Command command) {

    }

}
