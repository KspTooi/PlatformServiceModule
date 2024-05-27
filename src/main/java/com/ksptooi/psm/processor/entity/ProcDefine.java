package com.ksptooi.psm.processor.entity;

import lombok.Data;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 处理器中的请求映射实体
 */
@Data
public class ProcDefine {

    //定义类型(PROC中包含Hook与请求处理器(映射))
    private String defType;

    //请求处理器匹配模式(如果有)
    private String pattern;

    //请求处理器模式别名
    private List<String> alias;

    //请求处理器上面的形参数量
    private int paramCount;

    //请求处理器上面的形参名称
    private List<String> params;

    //函数签名
    private Method method;

    //处理器名称
    private String procName;

}
