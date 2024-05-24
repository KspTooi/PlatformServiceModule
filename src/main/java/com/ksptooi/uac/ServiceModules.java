package com.ksptooi.uac;

import com.ksptooi.guice.annotations.MultiInstance;
import com.ksptooi.guice.annotations.Unit;

@Unit
@MultiInstance
public class ServiceModules {

    public ServiceModules(){
        System.out.println("初始化");
    }

    public void test(){
        System.out.println("ok ready");
    }

}
