package com.ksptooi.psm.subsystem;

import com.ksptooi.Application;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.EventSchedule;
import com.ksptooi.psm.processor.ServiceUnitManager;
import com.ksptooi.psm.processor.SrvUnitTools;
import com.ksptooi.psm.processor.RequestProcessor;
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

    @Inject
    private EventSchedule eventSchedule;


    public void install(List<DiscoveredSubSystem> dss){
        for(var item : dss){

            if(exists(item.getName())){
                log.error("cannot install subsystem {} because the same name is already installed.",item.getName());
                continue;
            }

            //实例化子系统入口
            SubSystem entryInstance = null;

            try {
                var instance = item.getEntry().getDeclaredConstructor().newInstance();
                if(!(instance instanceof SubSystem)){
                    log.error("[无法安装] 子系统 {} 已损坏. 因为其入口没有继承自SubSystem.",item.getName());
                    continue;
                }
                entryInstance = (SubSystem) instance;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            //扫描子系统中定义的Processor
            Set<Class<?>> processorDefine = item.getReflections().getTypesAnnotatedWith(RequestProcessor.class);
            serviceUnitManager.register(SrvUnitTools.getProcessorInstance(processorDefine));
            serviceUnitManager.installRequestHandler();
            serviceUnitManager.installEventHandler();

            var activated = new ActivatedSubSystem();
            activated.setJarFile(item.getJarFile());
            activated.setName(item.getName());
            activated.setVersion(item.getVersion());
            activated.setEntry(entryInstance);
            activated.setClassLoader(item.getClassLoader());
            activated.setReflections(item.getReflections());
            activated.setProcessorDefine(processorDefine.stream().toList());
            installed.add(activated);

            log.info("子系统 {}-{} 已安装",item.getName(),item.getVersion());

            //给子系统入口注入内部组件
            Application.injector.injectMembers(entryInstance);
            //调用子系统安装完成钩子
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
