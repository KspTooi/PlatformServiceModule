package com.ksptooi.asf;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.modules.ApplicationModule;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.dispatcher.ProcessorDispatcher;
import com.ksptooi.asf.extendsbuildin.processor.PackManagerProcessor;
import com.ksptooi.asf.extendsbuildin.processor.PackRunnerProcessor;

public class ServiceFrame {

    public static Injector injector = Guice.createInjector(new ApplicationModule());


    public static void main(String[] args) throws Exception {

        //injector.createChildInjector(new ExtendsModules());

        PluginLoader epl = injector.getInstance(PluginLoader.class);

        epl.install(epl.getPlugin("plugins"));

        //注册基本命令
        ProcessorDispatcher scheduler = injector.getInstance(ProcessorDispatcher.class);
        scheduler.register("build-in-PackManagerExecutor",new PackManagerProcessor());
        scheduler.register("build-in-PackRunnerExecutor",new PackRunnerProcessor());

        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();
    }


}
