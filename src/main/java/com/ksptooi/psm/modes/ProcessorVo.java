package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessorVo {

    private Long id;

    private String name;

    private int status;

    private Date createTime;

}
