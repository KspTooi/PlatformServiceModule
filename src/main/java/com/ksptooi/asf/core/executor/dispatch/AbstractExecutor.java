package com.ksptooi.asf.core.executor.dispatch;

public abstract class AbstractExecutor implements Listener{

    @Override
    public void onInit() {

    }

    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

}
