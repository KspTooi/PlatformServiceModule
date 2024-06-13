package com.ksptooi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.ksptooi.guice.compomentscan.ComponentScanModule;
import com.ksptooi.psm.mybatis.DatabaseModule;
import com.ksptooi.psm.processor.ServiceUnitManager;
import com.ksptooi.psm.shell.SshModules;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.subsystem.SubSystemScanner;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class Application {

    public final static ComponentScanModule csm = new ComponentScanModule("com.ksptooi");

    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule(),csm);

    public final static String version = "4.0E/F";
    public final static String platform = "x64";

    public static void main(String[] p) throws InterruptedException {


        SubSystemScanner subSystemScanner = injector.getInstance(SubSystemScanner.class);
        var scan = subSystemScanner.scan("./subsystems");

        var subMgr = injector.getInstance(SubSystemManager.class);
        subMgr.install(scan);


        ServiceUnitManager unitMgr = injector.getInstance(ServiceUnitManager.class);
        unitMgr.scanFromPackage("com.ksptooi.psm");
        unitMgr.scanFromPackage("com.ksptooi.inner");
        unitMgr.installRequestHandler();
        unitMgr.installEventHandler();

        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }

}
