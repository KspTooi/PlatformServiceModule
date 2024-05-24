package com.ksptooi.uac.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.uac.core.cli.CommandLine;
import com.ksptooi.uac.core.cli.OperateCli;
import com.ksptooi.uac.core.plugins.PluginLoader;
import com.ksptooi.uac.core.processor.*;
import com.ksptooi.uac.core.plugins.JarPluginLoader;
import org.mybatis.guice.XMLMyBatisModule;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {


        //install(new ComponentScanModule("com.ksptooi", Comp.class));


        bind(ProcessorDispatcher.class).to(CmdBackgroundDispatcher.class).in(Scopes.SINGLETON);
        bind(CommandLine.class).to(OperateCli.class).in(Scopes.SINGLETON);
        bind(PluginLoader.class).to(JarPluginLoader.class).in(Scopes.SINGLETON);
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
