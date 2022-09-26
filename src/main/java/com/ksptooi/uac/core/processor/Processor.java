package com.ksptooi.uac.core.processor;

import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.CliCommand;

public interface Processor {

    public void onInit();

    public String[] defaultCommand();

    public void onCommand(CliCommand preparedCommand, Command command);

}
