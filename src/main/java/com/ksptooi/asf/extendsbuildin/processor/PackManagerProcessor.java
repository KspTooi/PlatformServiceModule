package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.service.PackManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Processor("build-in-PackManagerProcessor")
public class PackManagerProcessor extends AbstractProcessor {


    private final Logger logger = LoggerFactory.getLogger(PackManagerProcessor.class);


    @Inject
    private PackManagerService service;


    @Override
    public String[] defaultCommand() {
        return new String[]
                {
                 "auto",
                 "remove",
                 "pm lib set", //设置基准包目录
                 "pm lib add", //添加基准包目录
                 "pm lib clear",  //移除基准包目录
                 "pm lib",     //显示基准包目录
                 "pm scan",     //扫描基准包目录
                 "pm auto",
                 "pm remove",
                 "pm list",     //软件包列表
                 "pm search",   //搜索软件包
                };
    }



    @CommandMapping("pm lib set")
    public void setPath(CliCommand pCommand){

        if(pCommand.getParameter().size() < 1){
            logger.info("参数不足(name,path)");
            return;
        }

        if(pCommand.getParameter().size() == 1){
            service.setPackLib(null,pCommand.getParameter().get(0));
            return;
        }

        service.setPackLib(pCommand.getParameter().get(1),pCommand.getParameter().get(0));
    }

    @CommandMapping("pm lib clear")
    public void clearLibs(CliCommand pCommand){
        service.clearLibs();
    }


    @CommandMapping("pm lib")
    public void showLibs(){
        service.showPackLibs();
    }


    @CommandMapping("auto")
    public void auto(CliCommand pCommand, Command command){

        if(pCommand.getParameter().size() < 2){
            logger.info("参数不足(name,path)");
            return;
        }

        logger.info("正在从路径安装软件包...");
        service.autoInstall(pCommand.getParameter().get(0),pCommand.getParameter().get(1));
    }


    @CommandMapping("remove")
    public void remove(CliCommand pCommand, Command command){

        if(pCommand.getParameter().size() < 1){
            logger.info("参数不足(name)");
            return;
        }

        logger.info("正在移除软件包...");
        service.removePack(pCommand.getParameter().get(0));
    }


    @CommandMapping("pm auto")
    public void pmAuto(CliCommand pCommand, Command command){
        this.auto(pCommand,command);
    }

    @CommandMapping("pm remove")
    public void pmRemove(CliCommand pCommand, Command command){
        this.remove(pCommand,command);
    }


    @Override
    public void onCommand(CliCommand pCommand, Command command) {
    }




}
