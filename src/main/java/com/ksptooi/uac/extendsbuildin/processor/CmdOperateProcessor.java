package com.ksptooi.uac.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.extendsbuildin.service.CommandOperateService;

@Processor("build-in-CmdOperateProcessor")
public class CmdOperateProcessor extends ProcessorAdapter {


    @Inject
    private CommandOperateService service;

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cmd list",
                "cmd l",
                "cmd refresh",
                "cmd r",
                "help",
        };
    }


    @CommandMapping({"cmd l","cmd list"})
    public void list(){
        service.listAll();
    }

    @CommandMapping({"cmd refresh","cmd r"})
    public void refreshCommand(){
        service.refreshCommand();
    }

    @CommandMapping("help")
    public void helpCommand(){
        service.listAll();
    }



    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }

}
