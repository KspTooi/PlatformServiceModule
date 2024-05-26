package com.ksptooi.psm.processor.entity;

import com.ksptooi.psm.processor.Processor;
import lombok.Data;

@Data
public class ActiveProcessor {

    private String procName;

    private long procId;

    private Processor proc;

}
