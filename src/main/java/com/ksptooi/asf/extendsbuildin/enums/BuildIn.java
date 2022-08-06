package com.ksptooi.asf.extendsbuildin.enums;

public enum BuildIn {


    APP("build-in-ApplicationProcessor"),
    APP_RUNNER("build-in-ApplicationRunnerProcessor");






    private String processorName;

    BuildIn(String name) {
        this.processorName = name;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
}
