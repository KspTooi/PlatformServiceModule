package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class RequestVo {

    private Long id;
    private String name;
    private Integer parameter_count;
    private String parameters;
    private String metadata;
    private Long processorId;
    private Date createTime;

}
