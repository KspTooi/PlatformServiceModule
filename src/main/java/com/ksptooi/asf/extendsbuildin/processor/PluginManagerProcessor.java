package com.ksptooi.asf.extendsbuildin.processor;

import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.ProcessorAdapter;



@Processor("build-in-PluginManagerProcessor")
public class PluginManagerProcessor extends ProcessorAdapter {


    @Override
    public String[] defaultCommand() {
        return new String[]{

        };
    }




    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
