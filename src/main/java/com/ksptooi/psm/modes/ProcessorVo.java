package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProcessorVo {

    private Long id;

    private String name;

    private List<String> alias;

    private int status;

    private Date createTime;

    private String classType;

}
