package com.ksptooi.psm.subsystem;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.Application;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 子系统管理器
 */
@Unit
public class SubSystemManager {

    private static final Logger log = LoggerFactory.getLogger(SubSystemManager.class);

    private final List<ActivatedSubSystem> installed = new ArrayList<>();

    @Inject
    private ServiceUnitManager serviceUnitManager;

    public void install(Injector parentCtx, List<DiscoveredSubSystem> dss){
        for(var item : dss){

            if(exists(item.getName())){
                log.error("cannot install subsystem {}-{}({}) because the same name is already installed.",item.getName(),item.getVersion(),item.getJarFile().getName());
                continue;
            }

            //实例化子系统入口
            SubSystem entryInstance = null;

            try {
                var instance = item.getEntry().getDeclaredConstructor().newInstance();
                if(!(instance instanceof SubSystem)){
                    log.error("Cannot install subsystem {} that is damaged because its entry does not inherit from SubSystem",item.getName());
                    continue;
                }
                entryInstance = (SubSystem) instance;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            var ssl = new SubSystemLoaderModule(item,entryInstance);
            //var subInjector = parentCtx.createChildInjector(ssl);
            var subInjector = Guice.createInjector(ssl);

            var subSystem = ssl.getSubSystem();
            subSystem.setInjector(subInjector);

            try {
                serviceUnitManager.register(subInjector);
            } catch (ServiceUnitRegException e) {
                throw new RuntimeException(e);
            }

            installed.add(subSystem);
            entryInstance.onActivated();
        }
    }

    public void uninstall(ActivatedSubSystem ass){

    }

    public boolean exists(String name) {
        for (var subsystem : installed) {
            if (subsystem.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ActivatedSubSystem getSubSystem(String name){
        return null;
    }

    public ActivatedSubSystem getSubSystem(String n,String v){
        return null;
    }

}
