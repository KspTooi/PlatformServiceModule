package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.core.plugins.Plugin;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;

import java.util.Map;

public class PluginOperateService {

    @Inject
    private PluginLoader pluginLoader;

    @Inject
    private ProcessorDispatcher processorDispatcher;

    /**
     * 卸载所有插件
     */
    public void uninstallAll(){

        Map<String, Plugin> pluginList = pluginLoader.getPluginList();


    }


}
