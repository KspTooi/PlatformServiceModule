package com.ksptooi.wphub.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.wphub.command.CommandParser;
import com.ksptooi.wphub.command.InnerCommandParser;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {

        //install(new ComponentScanModule("com.ksptooi", Comp.class));

        bind(CommandParser.class).to(InnerCommandParser.class).in(Scopes.SINGLETON);

    }


}
