package com.ksptooi.wphub;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.wphub.cli.CommandLine;
import com.ksptooi.wphub.command.CommandParser;
import com.ksptooi.wphub.command.InnerCommandParser;
import com.ksptooi.wphub.entities.Command;
import com.ksptooi.wphub.executor.BasicExecutor;
import com.ksptooi.wphub.executor.dispatch.CommandScheduler;
import com.ksptooi.wphub.mapper.CommandMapper;
import com.ksptooi.wphub.modules.ApplicationModule;
import org.checkerframework.checker.units.qual.C;

public class ApplicationRunner {

    public static Injector injector = Guice.createInjector(new ApplicationModule());


    public static void main(String[] args) {


        //注册基本命令
/*        CommandScheduler scheduler = injector.getInstance(CommandScheduler.class);
        scheduler.addListener("wphub_basic",new BasicExecutor());


        CommandLine cli = injector.getInstance(CommandLine.class);
        cli.run();*/


        CommandMapper instance = injector.getInstance(CommandMapper.class);

        Command cmd = new Command();

        cmd.set



    }



}
