package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.shell.VirtualTextArea;
import lombok.Getter;

@Getter
public class AfterVirtualTextAreaChangeEvent extends AbstractServiceUnitEvent {

    private final PSMShell shell;
    private final VirtualTextArea virtualTextArea;
    private final String newContent;
    private final String oldContent;

    public AfterVirtualTextAreaChangeEvent(PSMShell shell,VirtualTextArea vt,String nContent,String oContent){
        this.shell = shell;
        this.virtualTextArea = vt;
        this.newContent = nContent;
        this.oldContent = oContent;
    }

    @Override
    public PSMShell getUserShell() {
        return shell;
    }

}
