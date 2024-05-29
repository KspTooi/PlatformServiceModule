package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.shell.ShellInstance;
import lombok.Getter;

@Getter
public class ShellInputEvent extends CancellableEvent{

    private ShellInstance user;
    private final char[] rawInput;
    private final int length;

    public ShellInputEvent(ShellInstance u, char[] c, int len){
        this.user = u;
        this.rawInput = c;
        this.length = len;
    }

}
