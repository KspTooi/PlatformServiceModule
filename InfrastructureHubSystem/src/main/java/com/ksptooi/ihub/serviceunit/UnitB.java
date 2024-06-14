package com.ksptooi.ihub.serviceunit;

import com.ksptooi.guice.annotations.Unit;
import jakarta.inject.Inject;

@Unit
public class UnitB {

    @Inject
    private UnitA ua;

    public void test(){
        System.out.println("UnitB::public void test(){");
    }

}
