package com.ksptooi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.psm.bootstrap.BootOptions;
import com.ksptooi.psm.bootstrap.Bootstrap;
import com.ksptooi.psm.bootstrap.BootstrapException;
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

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static Injector injector = null;

    public final static String version = "4.0P";
    public final static String platform = "x64";

    public static void main(String[] p) throws InterruptedException, ServiceUnitRegException, SQLException, BootstrapException {

        final var sts = System.currentTimeMillis();

        //加载引导文件
        var boot = Bootstrap.load("bootstrap.yml");
        createInjector(boot);

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

    private static void createInjector(BootOptions boot){

        var csm = new UnitLoaderModule("com.ksptooi");
        var sshd = new SshModules();
        var xmlMybatis = new DatabaseModule();

        if(injector == null){
            injector = Guice.createInjector(csm,xmlMybatis,sshd);
        }

    }


    public static Injector getInjector(){
        return injector;
    }

}
