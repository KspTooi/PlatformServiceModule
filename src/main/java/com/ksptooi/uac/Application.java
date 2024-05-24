package com.ksptooi.uac;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.psm.shell.SshModules;

import java.util.concurrent.CountDownLatch;

public class Application {


    public final static Injector injector = Guice.createInjector(new SshModules());


    public static void main(String[] args) throws InterruptedException {

        //SshServer instance = injector.getInstance(SshServer.class);


        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();

    }

}
