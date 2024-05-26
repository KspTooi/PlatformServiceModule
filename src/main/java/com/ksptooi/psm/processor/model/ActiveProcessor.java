package com.ksptooi.psm.processor.model;

import com.ksptooi.psm.processor.Processor;
import lombok.Data;

@Data
public class ActiveProcessor {

    private String procName;

    private long procId;

    private Processor proc;

}
