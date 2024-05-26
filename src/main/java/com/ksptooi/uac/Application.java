package com.ksptooi.uac;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.psm.mapper.ProcessorMapper;
import com.ksptooi.psm.modes.ProcessorVo;
import com.ksptooi.psm.mybatis.DatabaseModule;
import com.ksptooi.psm.processor.Processor;
import com.ksptooi.psm.processor.ProcessorManager;
import com.ksptooi.psm.processor.TestProcessor;
import com.ksptooi.psm.shell.SshModules;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Application {


    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule());


    public static void main(String[] args) throws InterruptedException {

        ProcessorManager instance = injector.getInstance(ProcessorManager.class);
        instance.scanFromPackage("com.ksptooi.psm");
        instance.installProcRequest();

        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }

}
