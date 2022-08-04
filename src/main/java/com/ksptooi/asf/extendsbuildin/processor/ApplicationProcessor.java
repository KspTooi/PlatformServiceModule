package com.ksptooi.asf.extendsbuildin.processor;

import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.AbstractProcessor;

@Processor("build-in-ApplicationProcessor")
public class ApplicationProcessor extends AbstractProcessor {





    @Override
    public String[] defaultCommand() {
        return new String[]{""};
    }




    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
