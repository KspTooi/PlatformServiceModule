package com.ksptooi.wphub;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.wphub.command.CommandParser;
import com.ksptooi.wphub.command.InnerCommandParser;
import com.ksptooi.wphub.modules.WpHubModule;

public class WpHubRunner {


    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new WpHubModule());

        CommandParser instance = injector.getInstance(CommandParser.class);

        System.out.println(instance);


    }



}
