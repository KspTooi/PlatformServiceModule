package com.ksptooi.wphub.command;

import com.ksptooi.wphub.annatatiotion.Component;
import com.ksptooi.wphub.entities.PreparedCommand;

@Component
public interface CommandParser {

    public PreparedCommand parse(String wphubCommandString);

}
