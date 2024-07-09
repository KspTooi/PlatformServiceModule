package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class CommandEntity {

    private Long id;
    private String pattern;
    private String serviceUnitName;
    private String serviceUnitClassType;
    private Integer status;
    private String metadata;
    private Date createdTime;

}
