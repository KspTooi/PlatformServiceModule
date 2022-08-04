package com.ksptooi.asf.extendsbuildin.processor;

import com.google.gson.Gson;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.entities.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Processor("build-in-ApplicationRunnerProcessor")
public class ApplicationRunnerProcessor extends AbstractProcessor {


    private final Logger logger = LoggerFactory.getLogger(ApplicationRunnerProcessor.class);


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        logger.info("运行软件包:" + preparedCommand);

        Application application = new Gson().fromJson(command.getMetadata(), Application.class);

        try {

            Runtime.getRuntime().exec("explorer " + application.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("运行软件包失败:" + preparedCommand.getName());
        }

    }

}
