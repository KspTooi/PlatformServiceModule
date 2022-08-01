package com.ksptooi.asf.core.executor;

import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.PreparedCommand;

public interface Listener {

    public void onInit();

    public String[] defaultCommand();

    public void onCommand(PreparedCommand preparedCommand, Command command);

}
