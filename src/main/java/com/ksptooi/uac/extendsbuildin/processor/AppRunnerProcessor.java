package com.ksptooi.uac.extendsbuildin.processor;

import com.google.gson.Gson;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.extendsbuildin.entities.ApplicationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Processor("build-in-AppRunnerProcessor")
public class AppRunnerProcessor extends ProcessorAdapter {


    private final Logger logger = LoggerFactory.getLogger(AppRunnerProcessor.class);


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        //logger.info("运行应用:" + preparedCommand);

        ApplicationData application = new Gson().fromJson(command.getMetadata(), ApplicationData.class);

        try {

            Process exec = Runtime.getRuntime().exec("explorer " + application.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("运行应用失败:" + preparedCommand.getName());
        }

    }

}
