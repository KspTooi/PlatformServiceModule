package com.ksptooi.asf.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.cli.OperateCli;
import com.ksptooi.asf.core.processor.dispatcher.CmdAnnotationDispatcher;
import com.ksptooi.asf.core.processor.dispatcher.CmdProcessRegisterWrapper;
import com.ksptooi.asf.core.processor.dispatcher.ProcessorDispatcher;
import com.ksptooi.asf.core.plugins.PluginLoader;
import org.mybatis.guice.XMLMyBatisModule;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {


        //install(new ComponentScanModule("com.ksptooi", Comp.class));

        //命令调度器
        bind(ProcessorDispatcher.class).to(CmdAnnotationDispatcher.class).in(Scopes.SINGLETON);

        //Cli
        bind(CommandLine.class).to(OperateCli.class).in(Scopes.SINGLETON);

        //EPL
        bind(PluginLoader.class).in(Scopes.SINGLETON);

        XMLMyBatisModule myBatisModule = new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId("prod");
                setClassPathResource("mybatis-config.xml");
            }
        };

        install(myBatisModule);


    }


}
