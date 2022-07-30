package com.ksptooi.wphub.executor.dispatch;

import com.ksptooi.wphub.entities.PreparedCommand;

public interface Listener {

    public void setName(String name);

    public void onCommand(PreparedCommand command);

}
