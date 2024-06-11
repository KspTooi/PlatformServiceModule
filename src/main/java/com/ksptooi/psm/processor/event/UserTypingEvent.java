package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

public class UserTypingEvent extends AbstractServiceUnitEvent {

    public final PSMShell userShell;

    @Getter
    private final char[] chars;
    @Getter
    private final int len;
    @Getter
    private final String content;



    public UserTypingEvent(PSMShell shell, char[] chars,int len,String content){
        this.userShell = shell;
        this.chars = chars;
        this.len = len;
        this.content = content;
    }

    @Override
    public PSMShell getUserShell() {
        return userShell;
    }

}
