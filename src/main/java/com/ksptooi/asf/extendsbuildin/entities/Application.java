package com.ksptooi.asf.extendsbuildin.entities;

public class Application {

    private final String metaVersion = "v1";

    private String path;

    public String getMetaVersion() {
        return metaVersion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
