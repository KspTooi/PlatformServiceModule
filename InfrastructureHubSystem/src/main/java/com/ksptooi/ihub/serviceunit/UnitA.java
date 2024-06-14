package com.ksptooi.ihub.serviceunit;

import com.ksptooi.guice.annotations.Unit;
import jakarta.inject.Inject;

@Unit
public class UnitA {

    @Inject
    private UnitB ub;

    public void test(){
        ub.test();
    }

}
