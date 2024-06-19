package com.ksptooi.psm.database;

import com.google.inject.AbstractModule;
import com.ksptooi.psm.bootstrap.BootOptions;

import java.io.File;

public class MybatisOptStartModules extends AbstractModule {

    private BootOptions opt;

    public MybatisOptStartModules(BootOptions opt){
        this.opt = opt;
    }

}
