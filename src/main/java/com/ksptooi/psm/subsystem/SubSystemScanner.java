package com.ksptooi.psm.subsystem;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;

import java.io.File;
import java.util.List;

@Unit
public class SubSystemScanner {

    public List<DiscoveredSubSystem> scan(File dir){
        return null;
    }


    public List<DiscoveredSubSystem> scan(String path){
        return null;
    }

}
