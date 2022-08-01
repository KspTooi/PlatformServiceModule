package com.ksptooi.asf.core.entities;

import java.util.List;
import java.util.Map;

public class PreparedCommand {

    //名称
    private String name;

    private List<String> parameter;

    //参数组
    private Map<String,String> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParameter() {
        return parameter;
    }

    public void setParameter(List<String> parameter) {
        this.parameter = parameter;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "PreparedCommand{" +
                "name='" + name + '\'' +
                ", parameter=" + parameter +
                ", parameters=" + parameters +
                '}';
    }
}
