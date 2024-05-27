package com.ksptooi.psm.processor.entity;

import com.ksptooi.psm.processor.Processor;
import lombok.Data;

import java.util.List;

@Data
public class ActiveProcessor {

    private String procName;

    private Object proc;

    private String classType;

    private List<ProcDefine> procDefines;

}