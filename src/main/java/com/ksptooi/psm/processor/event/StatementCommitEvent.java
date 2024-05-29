package com.ksptooi.psm.processor.event;

import com.ksptooi.psm.shell.ShellUser;
import lombok.Getter;

@Getter
public class StatementCommitEvent extends CancellableEvent{

    public final String statement;

    public final ShellUser user;

    public StatementCommitEvent(ShellUser user,String statement){
        this.user = user;
        this.statement = statement;
    }

}
