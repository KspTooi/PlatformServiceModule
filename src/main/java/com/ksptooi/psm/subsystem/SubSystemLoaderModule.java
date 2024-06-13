package com.ksptooi.psm.subsystem;

import com.google.inject.AbstractModule;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;

public class SubSystemLoaderModule extends AbstractModule {

    private DiscoveredSubSystem dSubSystem;

    private ActivatedSubSystem aSubSystem;

    public SubSystemLoaderModule(DiscoveredSubSystem dss){
        this.dSubSystem = dss;
    }

    @Override
    protected void configure() {

        //扫描子系统中的组件



    }
}
