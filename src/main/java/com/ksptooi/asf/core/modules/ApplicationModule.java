package com.ksptooi.asf.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.cli.OperateCli;
import com.ksptooi.asf.core.plugins.PluginLoader;
import com.ksptooi.asf.core.processor.*;
import com.ksptooi.asf.core.plugins.JarPluginLoader;
import org.mybatis.guice.XMLMyBatisModule;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {


        //install(new ComponentScanModule("com.ksptooi", Comp.class));

        //PD
        bind(ProcessorDispatcher.class).to(CmdBackgroundDispatcher.class).in(Scopes.SINGLETON);

        //Cli
        bind(CommandLine.class).to(OperateCli.class).in(Scopes.SINGLETON);

        //PL
        bind(PluginLoader.class).to(JarPluginLoader.class).in(Scopes.SINGLETON);

        //PS
        bind(ProcessorScanner.class).to(ProcessorAnnotationScanner.class).in(Scopes.SINGLETON);

        //

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
