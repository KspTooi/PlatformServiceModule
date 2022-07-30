package com.ksptooi.wphub.command;

import com.ksptooi.wphub.annatatiotion.Comp;
import com.ksptooi.wphub.entities.PreparedCommand;


public interface CommandParser {

    public PreparedCommand parse(String wphubCommandString);

}
