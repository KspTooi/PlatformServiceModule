package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.ProcessorAdapter;
import com.ksptooi.asf.extendsbuildin.service.PluginOperateService;


@Processor("build-in-PluginManagerProcessor")
public class PluginManagerProcessor extends ProcessorAdapter {


    @Inject
    private PluginOperateService service;


    @Override
    public String[] defaultCommand() {
        return new String[]{
                "plugin reload",
                "plugin list",
        };
    }

    @CommandMapping("plugin reload")
    public void pluginReload(){
        service.uninstallAll();
        service.installAll();
    }

    @CommandMapping("plugin list")
    public void pluginList(){
        service.printPluginList();
    }




    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }
}
