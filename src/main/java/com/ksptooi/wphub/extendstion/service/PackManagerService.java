package com.ksptooi.wphub.extendstion.service;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ksptooi.wphub.core.entities.Command;
import com.ksptooi.wphub.core.mapper.CommandMapper;
import com.ksptooi.wphub.core.service.CommandService;
import com.ksptooi.wphub.extendstion.entities.SoftwarePack;
import com.ksptooi.wphub.extendstion.executor.PackManagerExecutor;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

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

        boolean exists = Files.exists(Paths.get(path));

        if(!exists){
            logger.info("软件包安装失败,提供的Path不正确! \""+path+"\"");
            return;
        }


        SoftwarePack pack = new SoftwarePack();
        pack.setPath(path);


        Command insert = new Command();
        insert.setName(name);
        insert.setExecutorName("build-in-PackRunnerExecutor");
        insert.setMetadata(new Gson().toJson(pack));
        service.insert(insert);

        logger.info("软件包安装完成,指令为: \""+name+"\"");
    }


}
