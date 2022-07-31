package com.ksptooi.wphub.extendstion.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.wphub.extendstion.executor.PackManagerExecutor;
import com.ksptooi.wphub.extendstion.service.PackManagerService;

public class ExtendsModules extends AbstractModule {

    @Override
    protected void configure() {

        bind(PackManagerExecutor.class).in(Scopes.SINGLETON);
        bind(PackManagerService.class).in(Scopes.SINGLETON);

    }


}
