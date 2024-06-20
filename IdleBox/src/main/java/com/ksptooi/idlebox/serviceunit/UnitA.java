package com.ksptooi.idlebox.serviceunit;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.idlebox.mapper.ConfigSetMapper;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.subsystem.entity.ActivatedSubSystem;
import jakarta.inject.Inject;

@Unit
public class UnitA {

    @Inject
    private UnitB ub;

    @Inject
    private ConfigSetMapper csm;

    public void test(){
        ub.test();
    }

}
