package com.ksptooi.psm.subsystem;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SubSystemLoaderModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger("SSLM");

    private final DiscoveredSubSystem dSubSystem;
    private ActivatedSubSystem aSubSystem;
    private final SubSystem vEntry;

    public SubSystemLoaderModule(DiscoveredSubSystem dss,SubSystem vEntry){
        this.dSubSystem = dss;
        this.vEntry = vEntry;
    }

    @Override
    protected void configure() {

        //扫描子系统中的构件
        var ref = dSubSystem.getReflections();

        var units = ref.getTypesAnnotatedWith(Unit.class);
        var serviceUnits = ref.getTypesAnnotatedWith(ServiceUnit.class);

        log.info("Load [{}](Entry){}",dSubSystem.getName(),vEntry.getClass().getName());
        bind(SubSystem.class).toInstance(vEntry);

        for(var u : units){
            log.info("Load [{}](Unit){}",dSubSystem.getName(),u.getName());
            bind(u).in(Scopes.SINGLETON);
        }
        for (var u : serviceUnits){
            log.info("Load [{}](SrvUnit){}",dSubSystem.getName(),u.getName());
            bind(u).in(Scopes.SINGLETON);
        }

        aSubSystem = new ActivatedSubSystem();
        aSubSystem.setJarFile(dSubSystem.getJarFile());
        aSubSystem.setName(dSubSystem.getName());
        aSubSystem.setVersion(dSubSystem.getVersion());
        aSubSystem.setEntry(vEntry);
        aSubSystem.setClassLoader(dSubSystem.getClassLoader());
        aSubSystem.setReflections(dSubSystem.getReflections());
        aSubSystem.setInjector(null);
    }

    public ActivatedSubSystem getSubSystem(){
        return aSubSystem;
    }
}
