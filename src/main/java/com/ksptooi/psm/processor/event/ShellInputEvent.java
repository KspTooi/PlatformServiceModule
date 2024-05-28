package com.ksptooi.psm.processor.event;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ShellInputEvent extends CancellableEvent{

    private final char[] rawInput;
    private final int length;

    public ShellInputEvent(char[] c,int len){
        this.rawInput = c;
        this.length = len;
    }

}
