package com.ksptooi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.psm.database.DatabaseModule;
import com.ksptooi.psm.processor.ServiceUnitManager;
import com.ksptooi.psm.processor.ServiceUnitRegException;
import com.ksptooi.psm.shell.SshModules;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.subsystem.SubSystemScanner;
import com.ksptooi.psm.utils.UnitLoaderModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;


public class Application {

    public final static UnitLoaderModule csm = new UnitLoaderModule("com.ksptooi");
    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule(),csm);

    public final static String version = "4.0P";
    public final static String platform = "x64";
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] p) throws InterruptedException, ServiceUnitRegException, SQLException {

        final var sts = System.currentTimeMillis();

        //injector.getInstance(H2DatabaseUnit.class).start();

        var serviceUnitManager = injector.getInstance(ServiceUnitManager.class);
        var subSystemManager = injector.getInstance(SubSystemManager.class);
        var scanner =  injector.getInstance(SubSystemScanner.class);

        //加载内部组件
        serviceUnitManager.register(injector);

        //执行插件扫描
        var subSystems = scanner.scan("./subsystems");
        subSystemManager.install(injector,subSystems);

        final var ets = System.currentTimeMillis();
        log.info("Done({}ms)",(ets - sts));

        CountDownLatch cdl = new CountDownLatch(1);
        cdl.await();
    }

}
