package com.ksptooi.uac.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DatabaseService;
import com.ksptooi.uac.extendsbuildin.service.CommandOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

@Processor("build-in-CmdOperateProcessor")
public class CmdOperateProcessor extends ProcessorAdapter {

    private final Logger logger = LoggerFactory.getLogger(CmdOperateProcessor.class);

    @Inject
    private CommandOperateService service;

    @Inject
    private DatabaseService dbService;

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cmd list",
                "cmd l",
                "cmd refresh",
                "cmd r",
                "help",
                "db trim",
                "db export",
                "db import"
        };
    }

    @CommandMapping("db trim")
    public void dbTrim(){
        dbService.trim();
    }

    @CommandMapping("db export")
    public void dbExport(@Param("path")String path){

        Path p = null;

        try{
            p = Paths.get(path);
        }catch (Exception e){
            logger.info("无法解析路径:{}",path);
        }

        if(p == null){
            return;
        }

        dbService.export(p);
    }

    @CommandMapping("db import")
    public void dbImport(@Param("path")String path){

        Path p = null;

        try{
            p = Paths.get(path);
        }catch (Exception e){
            logger.info("无法解析路径:{}",path);
        }

        if(p == null){
            return;
        }

        dbService.dbImport(p);
    }

    @CommandMapping({"cmd l","cmd list"})
    public void list(){
        service.listAll();
    }

    @CommandMapping({"cmd refresh","cmd r"})
    public void refreshCommand(){
        service.refreshCommand();
    }

    @CommandMapping("help")
    public void helpCommand(){
        service.listAll();
    }



    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }

}
