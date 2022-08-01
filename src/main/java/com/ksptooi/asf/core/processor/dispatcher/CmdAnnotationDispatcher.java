package com.ksptooi.asf.core.processor.dispatcher;

import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.Processor;

import java.util.HashMap;

public class CmdAnnotationDispatcher extends CmdProcessRegisterWrapper{


    @Override
    public void publish(CliCommand inVo) {

        HashMap<String, Processor> processorMap = this.getProcessorMap();




        super.publish(inVo);
    }
}
