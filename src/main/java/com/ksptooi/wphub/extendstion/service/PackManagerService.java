package com.ksptooi.wphub.extendstion.service;

import com.google.inject.Inject;
import com.ksptooi.wphub.core.mapper.CommandMapper;
import com.ksptooi.wphub.core.service.CommandService;

public class PackManagerService {


    @Inject
    private CommandService service;


    public boolean checkBasicCommands(){

        if(service.hasCommand("auto")){

        }


        return true;
    }


}
