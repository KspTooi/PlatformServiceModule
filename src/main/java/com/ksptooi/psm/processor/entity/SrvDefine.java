package com.ksptooi.psm.processor.entity;

import lombok.Data;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 处理器中的请求映射实体
 */
@Data
public class SrvDefine implements Comparable<SrvDefine>{

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

    //服务单元名称
    private String srvUnitName;

    //事件处理器序列(如果有)
    private int eventHandlerOrder;

    //事件处理器类型
    private String eventHandlerType;

    //是否是全局事件处理器?
    private boolean globalEventHandler;

    //是否是后台事件处理器
    private boolean backgroundRequestHandler;

    //事件名称
    private String eventName;

    @Override
    public int compareTo(SrvDefine o) {
        return Integer.compare(this.eventHandlerOrder, o.eventHandlerOrder);
    }
}
