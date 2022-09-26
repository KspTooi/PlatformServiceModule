package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.core.entities.InstalledPlugin;
import com.ksptooi.asf.core.plugins.Plugin;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PluginOperateService {

    @Inject
    private PluginLoader pluginLoader;

    @Inject
    private ProcessorDispatcher processorDispatcher;


    public void printPluginList(){

        List<InstalledPlugin> pluginList = pluginLoader.getPluginList();

        CommandLineTable cliTable = new CommandLineTable();
        cliTable.setShowVerticalLines(true);
        cliTable.setHeaders("pluginName","version");

        pluginList.forEach(item->{
            cliTable.addRow(item.getName(), item.getVersion());
        });

        cliTable.print();

    }


    /**
     * 安装插件
     */
    public void installAll(){
        pluginLoader.install(pluginLoader.getJarPlugin("plugins"));
    }


    /**
     * 卸载所有插件
     */
    public void uninstallAll(){

        Logger logger = ServiceFrame.getLogger();

        List<InstalledPlugin> pluginList = new ArrayList<>(pluginLoader.getPluginList());


        logger.info("清除注册的插件处理器");

        for(InstalledPlugin plugin:pluginList){

            List<String> processors = plugin.getProcessors();

            for(String proc:processors){
                logger.info("移除处理器:{}",proc);
                processorDispatcher.remove(proc);
            }

        }

        logger.info("执行插件卸载");

        for(InstalledPlugin plugin:pluginList){
            plugin.getEntry().onDisable();
            pluginLoader.remove(plugin.getName());
            logger.info("已卸载插件:{}",plugin.getName());
        }

    }


}
