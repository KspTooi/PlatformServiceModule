package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class RequestDefineVo {

    private Long id;
    private String name;
    private Integer parameterCount;
    private String parameters;
    private String metadata;
    private Long processorId;
    private Date createTime;

}
