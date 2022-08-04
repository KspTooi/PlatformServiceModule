package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Param;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.CliCommandDefine;
import com.ksptooi.asf.core.entities.CliParam;
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


        new CliCommandDefine("",new CliParam("123"),new CliParam("123"));


        return new String[]{
                "app install",
                "app remove",
                "app i",
                "app rm",
        };
    }


    @CommandMapping({"auto","pm auto"})
    public void auto(CliCommand pCommand,Command command){

        if(pCommand.getParameter().size() < 2){
            logger.info("参数不足(name,path)");
            return;
        }

        logger.info("正在从路径安装软件包...");
        service.autoInstall(pCommand.getParameter().get(0),pCommand.getParameter().get(1));
    }




    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
