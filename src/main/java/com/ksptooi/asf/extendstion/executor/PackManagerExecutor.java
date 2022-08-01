package com.ksptooi.asf.extendstion.executor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.PreparedCommand;
import com.ksptooi.asf.core.executor.dispatch.AbstractExecutor;
import com.ksptooi.asf.extendstion.service.PackManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackManagerExecutor extends AbstractExecutor {


    private final Logger logger = LoggerFactory.getLogger(PackManagerExecutor.class);


    @Inject
    private PackManagerService service;


    @Override
    public String[] defaultCommand() {
        return new String[]
                {
                 "auto",
                 "remove",
                 "pm set path", //设置基准包目录
                 "pm add path", //添加基准包目录
                 "pm rm path",  //移除基准包目录
                 "pm scan",     //扫描基准包目录
                 "pm auto",
                 "pm remove",
                 "pm list",     //软件包列表
                 "pm search",   //搜索软件包
                };
    }


    @Override
    public void onCommand(PreparedCommand pCommand, Command command) {

        String cmdName = pCommand.getName();

        if(cmdName.equals("auto") || cmdName.equals("pm auto")){

            if(pCommand.getParameter().size() < 2){
                logger.info("参数不足(name,path)");
                return;
            }

            logger.info("正在从路径安装软件包...");
            service.autoInstall(pCommand.getParameter().get(0),pCommand.getParameter().get(1));
            return;
        }


        if(cmdName.equals("remove") || cmdName.equals("pm remove")){

            if(pCommand.getParameter().size() < 1){
                logger.info("参数不足(name)");
                return;
            }

            logger.info("正在移除软件包...");
            service.removePack(pCommand.getParameter().get(0));
            return;
        }

    }




}
