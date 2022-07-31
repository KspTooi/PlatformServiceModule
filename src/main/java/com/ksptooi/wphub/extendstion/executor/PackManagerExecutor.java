package com.ksptooi.wphub.extendstion.executor;

import com.google.inject.Inject;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.entities.PreparedCommand;
import com.ksptooi.wphub.core.executor.dispatch.AbstractExecutor;
import com.ksptooi.wphub.core.executor.dispatch.Listener;
import com.ksptooi.wphub.extendstion.service.PackManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackManagerExecutor extends AbstractExecutor {


    private final Logger logger = LoggerFactory.getLogger(PackManagerExecutor.class);


    @Inject
    private PackManagerService service;


    @Override
    public String[] defaultCommand() {
        return new String[]{"auto","remove","pm auto","pm remove"};
    }


    @Override
    public void onCommand(PreparedCommand pCommand, Command command) {

        String cmdName = pCommand.getName();

        if(cmdName.equals("auto") || cmdName.equals("pm auto")){

            if(pCommand.getParameter().size() < 2){
                logger.info("参数不足(path)");
                return;
            }

            logger.info("正在从路径安装软件包...");
            service.autoInstall(pCommand.getParameter().get(0),pCommand.getParameter().get(1));
            return;
        }


    }




}
