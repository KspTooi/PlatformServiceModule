package com.ksptooi.asf.core.command;

import com.ksptooi.asf.core.annatatiotion.Component;
import com.ksptooi.asf.core.entities.PreparedCommand;

@Component
public interface CommandParser {

    public PreparedCommand parse(String inCommandString);

}
