package com.ksptooi.ihub.serviceunit;

import com.ksptooi.psm.processor.EventHandler;
import com.ksptooi.psm.processor.RequestHandler;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceUnit("TestServiceUnit")
public class TestServiceUnit {


    private static final Logger log = LoggerFactory.getLogger(TestServiceUnit.class);

    @RequestHandler("handler")
    public void handler1(ShellRequest request){
        var cable = request.getCable();
        cable.connect(ConnectMode.OUTPUT);
        cable.w("OK").flush();
    }

    @EventHandler
    public void EventHandler(StatementCommitEvent event){
        log.info("public void EventHandler(StatementCommitEvent event)");
    }

}
