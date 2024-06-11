package com.ksptooi.psm.processor.event.generic;

import com.ksptooi.psm.shell.PSMShell;

public interface ServiceUnitEvent {

    public PSMShell getUserShell();

    public boolean isIntercepted();

    public boolean isCanceled();

}
