package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.processor.event.generic.AbstractServiceUnitEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

@Getter
public class StatementCommitEvent extends AbstractServiceUnitEvent {

    public final String statement;

    public final PSMShell userShell;

    public StatementCommitEvent(PSMShell shell, String statement){
        this.userShell = shell;
        this.statement = statement;
    }

    @Override
    public PSMShell getUserShell() {
        return userShell;
    }

}
