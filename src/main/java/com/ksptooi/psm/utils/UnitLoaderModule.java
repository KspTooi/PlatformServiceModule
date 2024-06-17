package com.ksptooi.psm.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.ServiceUnit;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class UnitLoaderModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger("ULM");

    private final String packageName;

    public UnitLoaderModule(String packageName){
        this.packageName = packageName;
    }

    @Override
    public void configure() {

        var packageRef = new Reflections(packageName);

        //扫描包中的构件
        var units = packageRef.getTypesAnnotatedWith(Unit.class);
        var serviceUnits = packageRef.getTypesAnnotatedWith(ServiceUnit.class);

        for(var u : units){
            log.info("Load Internal [Unit] {} ",u.getSimpleName());
            bind(u).in(Scopes.SINGLETON);
        }
        for (var u : serviceUnits){
            log.info("Load Internal [ServiceUnit] {}",u.getSimpleName());
            bind(u).in(Scopes.SINGLETON);
        }

    }
}
