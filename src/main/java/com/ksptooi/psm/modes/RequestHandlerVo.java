package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class RequestHandlerVo {

    private Long id;
    private String name;
    private Integer parameterCount;
    private String parameters;
    private String metadata;
    private String procName;
    private String procClassType;
    private Date createTime;

}
