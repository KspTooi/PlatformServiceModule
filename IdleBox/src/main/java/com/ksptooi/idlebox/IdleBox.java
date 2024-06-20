package com.ksptooi.idlebox;


import com.google.inject.Injector;
import com.ksptooi.Application;
import com.ksptooi.Platform;
import com.ksptooi.idlebox.serviceunit.UnitA;
import com.ksptooi.psm.database.MybatisXmlStartModules;
import com.ksptooi.psm.subsystem.*;
import com.ksptooi.psm.subsystem.Module;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

@SubSystemEntry(name = "IdleBox",version = "1.0A")
public class IdleBox extends SubSystem{

    private final Logger log = LoggerFactory.getLogger(IdleBox.class);

    @Inject
    private UnitA unit;

    @OnInstalled
    public void onActivated() {
        renderBanner();

        SubSystemManager ssm = Platform.getUnit(SubSystemManager.class);
        Injector idleBox = ssm.getSubSystem("IdleBox").getInjector();

    }

    @OnInstalled
    public void install(){

    }


    @Module
    public MybatisXmlStartModules mybatisXmlStartModules(){

        System.out.println(this.getClass().getClassLoader());
        var v = new MybatisXmlStartModules(this.getClass().getClassLoader());
        System.out.println(v.getClass().getClassLoader());

        return v;
    }






    public void renderBanner(){
        System.out.println("  _____       _   _          ____                 \n" +
                " |_   _|     | | | |        |  _ \\                \n" +
                "   | |     __| | | |   ___  | |_) |   ___   __  __\n" +
                "   | |    / _` | | |  / _ \\ |  _ <   / _ \\  \\ \\/ /\n" +
                "  _| |_  | (_| | | | |  __/ | |_) | | (_) |  >  < \n" +
                " |_____|  \\__,_| |_|  \\___| |____/   \\___/  /_/\\_\\\n" +
                "                                                  \n" +
                "                                                  ");
    }
}
