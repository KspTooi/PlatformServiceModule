package com.ksptooi.uac;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.guice.compomentscan.ComponentScanModule;
import com.ksptooi.uac.modules.SshMod;

import java.util.concurrent.CountDownLatch;

public class ApplicationSSHD {


    public final static ComponentScanModule csm = new ComponentScanModule("com.ksptooi");



    public final static Injector injector = Guice.createInjector(new SshMod(),csm);

    public static void main(String[] args) throws InterruptedException {


        injector.getInstance(ServiceModules.class).test();
        injector.getInstance(ServiceModules.class).test();

        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();

    }

}
