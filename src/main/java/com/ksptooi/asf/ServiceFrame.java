package com.ksptooi.asf;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.asf.core.annatatiotion.PluginEntry;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.executor.dispatch.CommandScheduler;
import com.ksptooi.asf.core.modules.ApplicationModule;
import com.ksptooi.asf.core.plugins.ExtendsPluginLoader;
import com.ksptooi.asf.extendstion.executor.PackManagerExecutor;
import com.ksptooi.asf.extendstion.executor.PackRunnerExecutor;

public class ServiceFrame {

    public static Injector injector = Guice.createInjector(new ApplicationModule());


    public static void main(String[] args) throws Exception {

        //injector.createChildInjector(new ExtendsModules());



/*
        ExtendsPluginLoader epl = injector.getInstance(ExtendsPluginLoader.class);

        epl.loadPlugin();*/

        //注册基本命令
/*        CommandScheduler scheduler = injector.getInstance(CommandScheduler.class);
        scheduler.addListener("build-in-PackManagerExecutor",new PackManagerExecutor());
        scheduler.addListener("build-in-PackRunnerExecutor",new PackRunnerExecutor());

        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();*/
    }


}
