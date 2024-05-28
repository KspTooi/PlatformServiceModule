package com.ksptooi.uac;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.guice.compomentscan.ComponentScanModule;
import com.ksptooi.psm.mybatis.DatabaseModule;
import com.ksptooi.psm.processor.ProcessorManager;
import com.ksptooi.psm.shell.SshModules;

import java.util.concurrent.CountDownLatch;

public class Application {

    public final static ComponentScanModule csm = new ComponentScanModule("com.ksptooi");

    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule(),csm);


    public static void main(String[] args) throws InterruptedException {

        ProcessorManager instance = injector.getInstance(ProcessorManager.class);
        instance.scanFromPackage("com.ksptooi.psm");
        instance.installRequestHandler();
        instance.installEventHandler();

        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }

}
