package com.ksptooi.wphub.core.service;

import com.google.inject.Inject;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.mapper.CommandMapper;

import java.util.List;

public class CommandService {

    @Inject
    private CommandMapper mapper;

    public Command getCommandByName(String name){

        Command query = new Command();
        query.setName(name);

        List<Command> commandList = mapper.getCommandList(query);

        if(commandList.size() < 1){
            return null;
        }

        return commandList.get(0);
    }


}
