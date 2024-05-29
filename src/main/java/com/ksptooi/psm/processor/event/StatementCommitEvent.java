package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.shell.ShellInstance;
import lombok.Getter;

@Getter
public class StatementCommitEvent extends CancellableEvent{

    public final String statement;

    public final ShellInstance user;

    public StatementCommitEvent(ShellInstance user, String statement){
        this.user = user;
        this.statement = statement;
    }

}
