package com.ksptooi.wphub.core.executor.dispatch;

import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.entities.PreparedCommand;

public interface Listener {

    public void onInit();

    public String[] defaultCommand();

    public void onCommand(PreparedCommand preparedCommand, Command command);

}
