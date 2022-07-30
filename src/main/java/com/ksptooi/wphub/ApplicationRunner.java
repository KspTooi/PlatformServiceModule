package com.ksptooi.wphub;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.wphub.command.CommandParser;
import com.ksptooi.wphub.command.InnerCommandParser;
import com.ksptooi.wphub.modules.ApplicationModule;

public class ApplicationRunner {


    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new ApplicationModule());

        CommandParser parser = injector.getInstance(CommandParser.class);





    }



}
