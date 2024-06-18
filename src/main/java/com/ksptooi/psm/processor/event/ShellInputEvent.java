package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class ShellInputEvent extends AbstractServiceUnitEvent {

    private final PSMShell userShell;
    private final char[] rawInput;
    private final int length;

    public ShellInputEvent(PSMShell u, char[] c, int len){
        this.userShell = u;
        this.rawInput = c;
        this.length = len;
    }

    public boolean match(int[] vk){
        return userShell.getCable().match(vk);
    }

    @Override
    public PSMShell getUserShell() {
        return userShell;
    }
}
