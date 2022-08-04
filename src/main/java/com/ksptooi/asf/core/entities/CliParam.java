package com.ksptooi.asf.core.entities;

public class CliParam {

    private String name;

    private String value;

    private String description;

    private boolean require = false;

    public CliParam(String name){
        this.name = name;
    }

    public CliParam(String name,String desc){
        this.name = name;
        this.description = desc;
    }

    public CliParam(String name,boolean require){
        this.name = name;
        this.require = require;
    }

    public CliParam(String name,String desc,boolean require){
        this.name = name;
        this.description = desc;
        this.require = require;
    }

    public Boolean getRequire() {
        return require;
    }

    public void setRequire(Boolean require) {
        this.require = require;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CliParam{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", require=" + require +
                '}';
    }
}
