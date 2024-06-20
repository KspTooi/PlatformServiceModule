package com.ksptooi.idlebox;


import com.ksptooi.Application;
import com.ksptooi.idlebox.serviceunit.UnitA;
import com.ksptooi.psm.database.MybatisXmlStartModules;
import com.ksptooi.psm.subsystem.Module;
import com.ksptooi.psm.subsystem.OnInstalled;
import com.ksptooi.psm.subsystem.SubSystem;
import com.ksptooi.psm.subsystem.SubSystemEntry;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SubSystemEntry(name = "IdleBox",version = "1.0A")
public class IdleBox {

    private final Logger log = LoggerFactory.getLogger(IdleBox.class);

    @Inject
    private UnitA unit;

    @OnInstalled
    public void onActivated() {
        renderBanner();
    }

    @OnInstalled
    public void install(){

    }




    @Module
    public MybatisXmlStartModules mybatisXmlStartModules(){
        return new MybatisXmlStartModules();
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
