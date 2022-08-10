package com.ksptooi.asf.extendsbuildin.processor;

import com.google.gson.Gson;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.ProcessorAdapter;
import com.ksptooi.asf.extendsbuildin.entities.ApplicationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Processor("build-in-ApplicationRunnerProcessor")
public class ApplicationRunnerProcessor extends ProcessorAdapter {


    private final Logger logger = LoggerFactory.getLogger(ApplicationRunnerProcessor.class);


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        logger.info("运行应用:" + preparedCommand);

        ApplicationData application = new Gson().fromJson(command.getMetadata(), ApplicationData.class);

        try {

            Runtime.getRuntime().exec("explorer " + application.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("运行应用失败:" + preparedCommand.getName());
        }

    }

}
