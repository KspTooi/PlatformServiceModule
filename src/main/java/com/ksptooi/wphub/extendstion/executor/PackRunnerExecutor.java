package com.ksptooi.wphub.extendstion.executor;

import com.google.gson.Gson;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.entities.PreparedCommand;
import com.ksptooi.wphub.core.executor.dispatch.AbstractExecutor;
import com.ksptooi.wphub.extendstion.entities.SoftwarePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PackRunnerExecutor extends AbstractExecutor {

    private final Logger logger = LoggerFactory.getLogger(PackRunnerExecutor.class);

    @Override
    public void onCommand(PreparedCommand preparedCommand, Command command) {

        logger.info("运行软件包:" + preparedCommand);

        SoftwarePack softwarePack = new Gson().fromJson(command.getMetadata(), SoftwarePack.class);

        try {

            Runtime.getRuntime().exec("explorer " + softwarePack.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("运行软件包失败:" + preparedCommand.getName());
        }

    }

}
