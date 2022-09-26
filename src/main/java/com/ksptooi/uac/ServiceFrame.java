package com.ksptooi.uac;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.uac.core.cli.CommandLine;
import com.ksptooi.uac.core.modules.ApplicationModule;
import com.ksptooi.uac.core.plugins.PluginLoader;
import com.ksptooi.uac.core.processor.Processor;
import com.ksptooi.uac.core.processor.ProcessorDispatcher;
import com.ksptooi.uac.core.processor.ProcessorScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ServiceFrame {

    public static Injector injector = Guice.createInjector(new ApplicationModule());

    public static final String version = "3.3A";

    private static final Logger logger = LoggerFactory.getLogger(ServiceFrame.class);

    public static Logger getLogger(){
        return logger;
    }

    public static void main(String[] args) throws Exception {


        PluginLoader pl = injector.getInstance(PluginLoader.class);
        ProcessorDispatcher scheduler = injector.getInstance(ProcessorDispatcher.class);
        ProcessorScanner processorScanner = injector.getInstance(ProcessorScanner.class);
        
        pl.install(pl.getJarPlugin("plugins"));

        //epl.install(epl.getPlugin("plugins"));

        Map<String, Processor> scan = processorScanner.scan("com.ksptooi.uac");
        scheduler.register(scan);

        //注册基本命令
        getLogger().info("服务平台版本:{}",version);

        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();
    }


}
