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
        this.seqArgument = r.getSeqArgument();
        this.argumentMap = r.getArgumentMap();
        this.shell = r.getShell();
        this.cable = r.getCable();
    }

    //请求原始语句
    private String statement;

    //请求Pattern
    private String pattern;

    //请求参数
    private List<String> seqArgument;

    //是否为顺序风格的参数输入
    private boolean seqStyleArgument;

    //请求参数组
    private Map<String,List<String>> argumentMap;

    //请求元数据
    private String metadata;

    private PSMShell shell;

    private AdvInputOutputCable cable;

    public String getArgumentMap(String kind){
        var val = argumentMap.get(kind);
        if(val == null || val.isEmpty()){
            return null;
        }
        return argumentMap.get(kind).getFirst();
    }

    public List<String> getArgument(String kind){
        var val = argumentMap.get(kind);
        if(val == null || val.isEmpty()){
            return null;
        }
        return argumentMap.get(kind);
    }

    public boolean hasArgument(String kind){
        return argumentMap.containsKey(kind);
    }
}
