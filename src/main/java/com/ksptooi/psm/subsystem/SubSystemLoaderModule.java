package com.ksptooi.psm.subsystem;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;
import org.reflections.Reflections;

import java.util.Set;

public class SubSystemLoaderModule extends AbstractModule {

    private DiscoveredSubSystem dSubSystem;
    private ActivatedSubSystem aSubSystem;
    private Object vEntry;

    public SubSystemLoaderModule(DiscoveredSubSystem dss,Object vEntry){
        this.dSubSystem = dss;
        this.vEntry = vEntry;
    }

    @Override
    protected void configure() {

        //扫描子系统中的构件
        var ref = dSubSystem.getReflections();

        var units = ref.getTypesAnnotatedWith(Unit.class);
        var serviceUnits = ref.getTypesAnnotatedWith(ServiceUnit.class);

        for(var u : units){
            bind(u).in(Scopes.SINGLETON);
        }

        for (var u : serviceUnits){
            bind(u).in(Scopes.SINGLETON);
        }


    }
}
