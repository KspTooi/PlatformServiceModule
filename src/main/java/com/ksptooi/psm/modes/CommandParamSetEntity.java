package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class CommandParamSetEntity {

    private Long id;

    private Long commandId;

    private String paramName;

    private String paramType;

    private String defaultValue;

    private String hintValue;

    private String validValue;

    private String introduction;

    private Date createTime;

}
