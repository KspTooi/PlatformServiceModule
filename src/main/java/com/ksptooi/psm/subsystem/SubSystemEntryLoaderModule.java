package com.ksptooi.psm.subsystem;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class SubSystemEntryLoaderModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger("SSELM");

    private DiscoveredSubSystem dss;
    private SubSystem vEntry;

    public SubSystemEntryLoaderModule(DiscoveredSubSystem dss, SubSystem vEntry){
        this.dss = dss;
        this.vEntry = vEntry;
    }

    @Override
    protected void configure() {

        var oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(dss.getClassLoader());

        log.info("Load [{}][Entry] {}",dss.getName(),vEntry.getClass().getSimpleName());
        bind(SubSystem.class).toInstance(vEntry);

        var modules = new ArrayList<Method>();
        SubSystems.findModules(vEntry,modules);

        for(var m : modules){

            try {

                var invoke = m.invoke(vEntry);

                if(invoke instanceof com.google.inject.Module){
                    install((Module) invoke);
                    log.info("Load [{}][Module] {}",dss.getName(),m.getName());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Thread.currentThread().setContextClassLoader(oldLoader);
    }
}
