package com.ksptooi.asf.core.processor;

public abstract class AbstractProcessor implements Processor {

    @Override
    public void onInit() {

    }

    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

}
