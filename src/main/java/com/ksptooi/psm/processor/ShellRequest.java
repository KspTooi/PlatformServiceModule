package com.ksptooi.psm.processor;

import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ShellRequest {

    public ShellRequest(){
    }

    public ShellRequest(String statement){
        this.statement = statement;
    }

    public ShellRequest(ShellRequest r){
        this.statement = r.getStatement();
        this.pattern = r.getPattern();
        this.params = r.getParams();
        this.parameterMap = r.getParameterMap();
        this.shell = r.getShell();
        this.cable = r.getCable();
    }

    //请求原始语句
    private String statement;

    //请求Pattern
    private String pattern;

    //请求参数
    private List<String> params;

    //请求参数组
    private Map<String,List<String>> parameterMap;

    //请求元数据
    private String metadata;

    private PSMShell shell;

    private AdvInputOutputCable cable;

    public String getParameter(String kind){
        var val = parameterMap.get(kind);
        if(val == null || val.isEmpty()){
            return null;
        }
        return parameterMap.get(kind).getFirst();
    }

    public List<String> getParameters(String kind){
        var val = parameterMap.get(kind);
        if(val == null || val.isEmpty()){
            return null;
        }
        return parameterMap.get(kind);
    }

    public boolean hasParameter(String kind){
        return parameterMap.containsKey(kind);
    }

}
