package com.ksptooi.psm.processor.entity;

import lombok.Data;

import java.util.List;

@Data
public class ActiveProcessor {

    private String procName;

    private Object proc;

    private String classType;

    private List<ProcDefine> procDefines;

    private boolean eventHandlerInstalled = false;

    private boolean requestHandlerInstalled = false;

}
