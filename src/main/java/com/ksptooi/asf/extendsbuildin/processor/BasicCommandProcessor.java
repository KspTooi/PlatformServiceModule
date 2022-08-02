package com.ksptooi.asf.extendsbuildin.processor;

import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.AbstractProcessor;

@Processor("build-in-BasicCommandProcessor")
public class BasicCommandProcessor extends AbstractProcessor {



    @CommandMapping("list")
    public void list(){

    }


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {
        
    }

}
