package com.ksptooi.asf;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.modules.ApplicationModule;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.Processor;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import com.ksptooi.asf.core.processor.ProcessorScanner;
import com.ksptooi.asf.extendsbuildin.processor.PackManagerProcessor;
import com.ksptooi.asf.extendsbuildin.processor.PackRunnerProcessor;

import java.util.Map;


public class ServiceFrame {

    public static Injector injector = Guice.createInjector(new ApplicationModule());


    public static void main(String[] args) throws Exception {

        PluginLoader epl = injector.getInstance(PluginLoader.class);
        ProcessorDispatcher scheduler = injector.getInstance(ProcessorDispatcher.class);
        ProcessorScanner processorScanner = injector.getInstance(ProcessorScanner.class);

        epl.install(epl.getPlugin("plugins"));

        Map<String, Processor> scan = processorScanner.scan("com.ksptooi.asf");




        System.out.println(scan);

        //注册基本命令

        scheduler.register("build-in-PackManagerExecutor",new PackManagerProcessor());
        scheduler.register("build-in-PackRunnerExecutor",new PackRunnerProcessor());

        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();
    }


}
