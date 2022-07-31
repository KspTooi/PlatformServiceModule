package com.ksptooi.wphub.core.command;

import com.ksptooi.wphub.core.annatatiotion.Component;
import com.ksptooi.wphub.core.entities.PreparedCommand;

@Component
public interface CommandParser {

    public PreparedCommand parse(String inCommandString);

}
