package com.ksptooi.uac.core.processor;

public abstract class ProcessorAdapter implements Processor {

    @Override
    public void onInit() {

    }

    @Override
    public String[] defaultCommand() {
        return new String[0];
    }

}
