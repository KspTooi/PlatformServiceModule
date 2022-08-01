package com.ksptooi.asf.extendsbuildin.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.service.CommandService;
import com.ksptooi.asf.extendsbuildin.entities.PackLibrary;
import com.ksptooi.asf.extendsbuildin.entities.PackLibraryDocument;
import com.ksptooi.asf.extendsbuildin.entities.SoftwarePack;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PackManagerService {

    private final Logger logger = LoggerFactory.getLogger(PackManagerService.class);

    @Inject
    private CommandService service;


    private final String packLibrayKey = "#pack_libray";


    //设置软件包目录
    public void setPackLib(String name,String path){

        PackLibrary library = new PackLibrary();
        List<PackLibrary> libraryList = new ArrayList<>();

        if(name == null){
            library.setName("软件包目录");
        }
        library.setPath(path);
        libraryList.add(library);

        //软件包库记录不存在则增加
        if(!service.hasCommand(packLibrayKey)){
            Command librayDocument = new Command();
            librayDocument.setName(this.packLibrayKey);
            librayDocument.setMetadata(null);
            librayDocument.setExecutorName("#document");
            service.insert(librayDocument);
        }

        Command update = service.getCommandByName(this.packLibrayKey);
        update.setMetadata(new Gson().toJson(libraryList));
        service.update(update);
    }

    //显示软件包目录
    public void showPackLibs(){

        if(!service.hasCommand(this.packLibrayKey)){
            logger.info("当前没有设置软件包基准目录");
            return;
        }

        Command commandByName = service.getCommandByName(this.packLibrayKey);

        List<PackLibrary> list = JSON.parseArray(commandByName.getMetadata(),PackLibrary.class);

        System.out.println("当前软件包目录");

        for(PackLibrary item:list){
            System.out.println(item.getName() + "--" + item.getPath());
        }

    }

    public void clearLibs(){

        if(!service.hasCommand(this.packLibrayKey)){
            logger.info("当前没有设置软件包基准目录");
            return;
        }

        Command commandByName = service.getCommandByName(this.packLibrayKey);
        commandByName.setMetadata("[]");
        service.update(commandByName);

        logger.info("软件包基准目录已被清除");
    }



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

    //移除软件包
    public void removePack(String name){

        Command commandByName = service.getCommandByName(name);

        if(commandByName == null){
            logger.info("软件包移除失败,指令\""+name+"\"不存在!");
            return;
        }


        service.removeById(commandByName.getCmdId()+"");
        logger.info("软件包\""+name+"\"移除成功!");
    }


}
