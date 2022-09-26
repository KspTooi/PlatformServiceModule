package com.ksptooi.uac.extendsbuildin.enums;

public enum BuildIn {


    APP("build-in-AppProcessor"),
    APP_RUNNER("build-in-AppRunnerProcessor");






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
