package com.ksptooi.asf.extendsbuildin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.asf.extendsbuildin.executor.PackManagerExecutor;
import com.ksptooi.asf.extendsbuildin.service.PackManagerService;

public class ExtendsModules extends AbstractModule {

    @Override
    protected void configure() {

        bind(PackManagerExecutor.class).in(Scopes.SINGLETON);
        bind(PackManagerService.class).in(Scopes.SINGLETON);

    }


}
