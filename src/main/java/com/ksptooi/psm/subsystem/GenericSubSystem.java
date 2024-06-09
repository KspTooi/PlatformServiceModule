package com.ksptooi.psm.subsystem;
import com.ksptooi.psm.processor.EventSchedule;
import com.ksptooi.psm.processor.ProcessorManager;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SubSystemEntry(name = "GenericSubSystem",version = "1.0A")
public class GenericSubSystem extends SubSystem{

    private final static Logger log = LoggerFactory.getLogger(GenericSubSystem.class);

    @Inject
    private ProcessorManager processorManager;

    @Inject
    private EventSchedule eventSchedule;

    @Override
    public void onActivated() {

        log.info("子系统被安装");
        processorManager.register(MyProcessor.class);

    }

}
