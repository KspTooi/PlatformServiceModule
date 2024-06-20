package com.ksptooi.idlebox.serviceunit;

import com.google.inject.Inject;
import com.ksptooi.Platform;
import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.subsystem.SubSystemManager;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceUnit("TestServiceUnit")
public class TestServiceUnit {


    private static final Logger log = LoggerFactory.getLogger(TestServiceUnit.class);

    @Inject
    private EventSchedule schedule;

    @RequestHandler("handler")
    public void handler1(ShellRequest request){

        var instance = Platform.getUnit(EventSchedule.class);
        var sub = Platform.getUnit(SubSystemManager.class);
        var cable = request.getCable();
        cable.connect(ConnectMode.OUTPUT);
        cable.w("OK").flush();
    }

    @EventHandler
    public void EventHandler(StatementCommitEvent event){
        log.info("public void EventHandler(StatementCommitEvent event)");
    }

}
