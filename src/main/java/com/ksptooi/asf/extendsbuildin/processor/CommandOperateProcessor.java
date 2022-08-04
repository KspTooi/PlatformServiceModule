package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.service.CommandOperateService;

@Processor("build-in-CommandOperateProcessor")
public class CommandOperateProcessor extends AbstractProcessor {


    @Inject
    private CommandOperateService service;

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cmd list",
                "cmd l",
                "cmd refresh"
        };
    }

    @CommandMapping({"cmd l","cmd list"})
    public void list(){
        service.listAll();
    }

    @CommandMapping("cmd refresh")
    public void refreshCommand(){
        service.refreshCommand();
    }



    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }

}
