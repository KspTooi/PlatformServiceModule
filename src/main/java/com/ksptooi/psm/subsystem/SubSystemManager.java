package com.ksptooi.psm.subsystem;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * 子系统管理器
 */
@Unit
public class SubSystemManager {


    private final List<ActivatedSubSystem> installed = new ArrayList<>();


    public void install(DiscoveredSubSystem dss){

    }


    public void uninstall(ActivatedSubSystem ass){

    }


    public ActivatedSubSystem getSubSystem(String name){
        return null;
    }

    public ActivatedSubSystem getSubSystem(String n,String v){
        return null;
    }

}
