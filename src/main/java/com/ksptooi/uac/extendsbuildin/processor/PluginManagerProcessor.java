package com.ksptooi.uac.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.extendsbuildin.service.PluginOperateService;


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
