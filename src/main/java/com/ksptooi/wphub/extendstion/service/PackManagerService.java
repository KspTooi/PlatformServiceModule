package com.ksptooi.wphub.extendstion.service;

import com.google.inject.Inject;
import com.ksptooi.wphub.core.mapper.CommandMapper;
import com.ksptooi.wphub.core.service.CommandService;
import com.ksptooi.wphub.extendstion.executor.PackManagerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackManagerService {

    private final Logger logger = LoggerFactory.getLogger(PackManagerService.class);

    @Inject
    private CommandService service;


    //自动安装包
    public void autoInstall(String name,String path){

        if(service.hasCommand(name)){
            logger.info("软件包安装失败,指令\""+name+"\"已被占用");
            return;
        }

        

    }


}
