package com.ksptooi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.guice.compomentscan.ComponentScanModule;
import com.ksptooi.psm.mybatis.DatabaseModule;
import com.ksptooi.psm.processor.ServiceUnitManager;
import com.ksptooi.psm.processor.ServiceUnitRegException;
import com.ksptooi.psm.shell.SshModules;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.subsystem.SubSystemScanner;
import com.ksptooi.psm.utils.UnitLoaderModule;

import java.util.concurrent.CountDownLatch;


public class Application {

    public final static UnitLoaderModule csm = new UnitLoaderModule("com.ksptooi");

    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule(),csm);

    public final static String version = "4.0G";
    public final static String platform = "x64";

    public static void main(String[] p) throws InterruptedException, ServiceUnitRegException {


        var serviceUnitMgr = injector.getInstance(ServiceUnitManager.class);
        serviceUnitMgr.register(injector);

        var scan = injector.getInstance(SubSystemScanner.class);
        var subSystems = scan.scan("./subsystems");
        var subManager = injector.getInstance(SubSystemManager.class);
        subManager.install(injector,subSystems);

/*        var subMgr = injector.getInstance(SubSystemManager.class);
        subMgr.install(injector,scan);


        ServiceUnitManager unitMgr = injector.getInstance(ServiceUnitManager.class);
        unitMgr.scanFromPackage("com.ksptooi.psm");
        unitMgr.scanFromPackage("com.ksptooi.inner");
        unitMgr.installRequestHandler();
        unitMgr.installEventHandler();
*/
        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }

}
