package com.ksptooi.wphub.entities;

import java.util.HashMap;

public class PreparedCommand {

    //名称
    private String name;

    //参数组
    private HashMap<String,String> parameter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(HashMap<String, String> parameter) {
        this.parameter = parameter;
    }
}
