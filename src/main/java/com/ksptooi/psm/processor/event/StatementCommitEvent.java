package com.ksptooi.psm.processor.event;

import lombok.Getter;

@Getter
public class StatementCommitEvent extends CancellableEvent{

    public String statement;

    public StatementCommitEvent(String statement){
        this.statement = statement;
    }

}
