package com.ksptooi.asf.core.processor;

import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;

public interface Processor {

    public void onInit();

    public String[] defaultCommand();

    public void onCommand(CliCommand preparedCommand, Command command);

}
