package com.ksptooi.psm.processor;

import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.shell.ShellInstance;
import com.ksptooi.psm.vk.AdvInputOutputStream;
import com.ksptooi.psm.vk.ShellVK;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProcRequest {

    public ProcRequest(){
    }

    public ProcRequest(ProcRequest r){
        this.statement = r.getStatement();
        this.pattern = r.getPattern();
        this.params = r.getParams();
        this.parameters = r.getParameters();
        this.shell = r.getShell();
        this.aio = r.getAio();
    }

    //请求原始语句
    private String statement;

    //请求Pattern
    private String pattern;

    //请求参数
    private List<String> params;

    //请求参数组
    private Map<String,String> parameters;

    private PSMShell shell;

    //private ShellVK shellVk;

    private AdvInputOutputStream aio;

}
