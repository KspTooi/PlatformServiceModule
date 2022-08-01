package com.ksptooi.asf.core.executor;

public abstract class AbstractExecutor implements Listener{

    @Override
    public void onInit() {

    }

    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

}
