package com.ksptooi.asf;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.modules.ApplicationModule;
import com.ksptooi.asf.core.plugins.JarPluginLoader;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.Processor;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import com.ksptooi.asf.core.processor.ProcessorScanner;
import com.ksptooi.asf.extendsbuildin.processor.PackManagerProcessor;
import com.ksptooi.asf.extendsbuildin.processor.PackRunnerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class ServiceFrame {

    public static Injector injector = Guice.createInjector(new ApplicationModule());

    public static final String version = "3.2A";

    private static final Logger logger = LoggerFactory.getLogger(ServiceFrame.class);

    public static void main(String[] args) throws Exception {

        PluginLoader pl = injector.getInstance(PluginLoader.class);
        ProcessorDispatcher scheduler = injector.getInstance(ProcessorDispatcher.class);
        ProcessorScanner processorScanner = injector.getInstance(ProcessorScanner.class);

        pl.install(pl.getJarPlugin("plugins"));

        //epl.install(epl.getPlugin("plugins"));

        Map<String, Processor> scan = processorScanner.scan("com.ksptooi.asf");
        scheduler.register(scan);


        //注册基本命令
        logger.info("服务平台版本:{}",version);

        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();


    }


}
