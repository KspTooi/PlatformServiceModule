package com.ksptooi.uac.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.extendsbuildin.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Processor("build-in-AppProcessor")
public class AppProcessor extends ProcessorAdapter {

    private final Logger logger = LoggerFactory.getLogger(AppProcessor.class);

    @Inject
    private ApplicationService service;


    @CommandMapping({"app i","app install"})
    public void appInstall(@Param("appName")String appName,
                           @Param("path")String path,CliCommand inCommand){

        String save = null;

        if(inCommand.getParameter().size()>2){
            save = inCommand.getParameter().get(2);
        }


        logger.info("正在从路径安装应用...");
        Command command = service.appInstall(appName, path);

        if(save == null || command == null){
            return;
        }

        if (save.equals("save")){
            logger.info("正在保存应用...");
            service.saveAsDocument(command);
        }

    }

    @CommandMapping({"app rm","app remove"})
    public void appRemove(@Param("appName")String appName){
        service.appRemove(appName);
    }

    @CommandMapping({"app l","app list"})
    public void appList(CliCommand preparedCommand){
        service.appShow();
    }

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "app install",
                "app i",
                "app remove",
                "app rm",
                "app list",
                "app l",
                "app install save",
                "app i s",
        };
    }


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
