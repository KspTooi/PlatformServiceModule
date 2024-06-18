package com.ksptooi.idlebox.serviceunit;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import jakarta.inject.Inject;

@Unit
public class UnitA {


    @Inject
    private SubSystemManager ssMgr;

    @Inject
    private UnitB ub;

    public void test(){

        var mod = ssMgr.getSubSystem("Module1");


        ub.test();
    }

}
