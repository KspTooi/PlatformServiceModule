package com.ksptooi.psm.processor.entity;

import lombok.Data;

import java.util.List;

/**
 * 处理器中的请求映射实体
 */
@Data
public class RequestDefine {

    private String name;

    private String procName;

    private List<String> alias;

    private int parameterCount;

    private List<String> parameters;

}
