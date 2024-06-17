package com.ksptooi.psm.processor.entity;

import lombok.Data;
import java.util.List;

@Data
public class ActivatedSrvUnit {

    private String srvUnitName;

    private Object srvUnit;

    private String classType;

    private List<SrvDefine> srvDefines;

    private boolean eventHandlerInstalled = false;

    private boolean requestHandlerInstalled = false;

}
