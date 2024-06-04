package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractProcEvent;
import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.shell.ShellInstance;
import lombok.Getter;

@Getter
public class ShellInputEvent extends AbstractProcEvent {

    private final PSMShell userShell;
    private final char[] rawInput;
    private final int length;

    public ShellInputEvent(PSMShell u, char[] c, int len){
        this.userShell = u;
        this.rawInput = c;
        this.length = len;
    }

    @Override
    public PSMShell getUserShell() {
        return userShell;
    }
}
