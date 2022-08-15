package com.ksptooi.asf.extendsbuildin.processor;

import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.annatatiotion.ServiceProcessor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.ProcessorAdapter;

@Processor("build-in-Dev-BackgroundProcessor")
@ServiceProcessor
public class BackgroundProcessor extends ProcessorAdapter {

    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }

}
