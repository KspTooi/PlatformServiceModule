package com.ksptooi.wphub.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.wphub.core.cli.CommandLine;
import com.ksptooi.wphub.core.cli.OperateCli;
import com.ksptooi.wphub.core.command.CommandParser;
import com.ksptooi.wphub.core.command.InnerCommandParser;
import com.ksptooi.wphub.core.executor.dispatch.CommandExclusiveWrapper;
import com.ksptooi.wphub.core.executor.dispatch.CommandRegisterWrapper;
import com.ksptooi.wphub.core.executor.dispatch.CommandScheduler;
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
