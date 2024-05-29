package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.shell.ShellUser;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ShellInputEvent extends CancellableEvent{

    private ShellUser user;
    private final char[] rawInput;
    private final int length;

    public ShellInputEvent(ShellUser u, char[] c, int len){
        this.user = u;
        this.rawInput = c;
        this.length = len;
    }

}
