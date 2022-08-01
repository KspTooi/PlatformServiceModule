package com.ksptooi.asf.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.asf.core.cli.CommandLine;
import com.ksptooi.asf.core.cli.OperateCli;
import com.ksptooi.asf.core.command.CommandParser;
import com.ksptooi.asf.core.command.InnerCommandParser;
import com.ksptooi.asf.core.executor.dispatch.CommandRegisterWrapper;
import com.ksptooi.asf.core.executor.dispatch.CommandScheduler;
import org.mybatis.guice.XMLMyBatisModule;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {


        //install(new ComponentScanModule("com.ksptooi", Comp.class));

        //命令解析器
        bind(CommandParser.class).to(InnerCommandParser.class).in(Scopes.SINGLETON);

        //命令调度器
        bind(CommandScheduler.class).to(CommandRegisterWrapper.class).in(Scopes.SINGLETON);

        //Cli
        bind(CommandLine.class).to(OperateCli.class).in(Scopes.SINGLETON);



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
