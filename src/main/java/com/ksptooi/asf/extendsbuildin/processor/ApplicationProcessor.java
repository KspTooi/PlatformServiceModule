package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Param;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Processor("build-in-ApplicationProcessor")
public class ApplicationProcessor extends AbstractProcessor {


    private final Logger logger = LoggerFactory.getLogger(ApplicationProcessor.class);

    @Inject
    private ApplicationService service;


    @Override
    public String[] defaultCommand() {
        return new String[]{
                "app install",
                "app i",
                "app remove",
                "app rm",
                "app list",
                "app l",
        };
    }

    @CommandMapping({"app i","app install"})
    public void appInstall(@Param("appName")String appName,
                           @Param("path")String path){

        logger.info("正在从路径安装应用...");
        service.appInstall(appName,path);
    }

    @CommandMapping({"app rm","app remove"})
    public void appRemove(@Param("appName")String appName){
        service.appRemove(appName);
    }

    @CommandMapping({"app l","app list"})
    public void appList(CliCommand preparedCommand){



    }


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
