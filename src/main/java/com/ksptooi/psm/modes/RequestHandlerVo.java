package com.ksptooi.psm.modes;

import lombok.Data;
import java.util.Date;

@Data
public class RequestHandlerVo {

    //ID
    private Long id;

    //匹配模式
    private String pattern;

    //参数
    private String params;

    //参数长度
    private Integer paramsCount;

    //服务单元名称
    private String srvUnitName;

    //服务单元ClassType
    private String srvUnitClassType;

    //状态 0:正常 1:被删除
    private Integer status;

    //元数据
    private String metadata;

    //创建时间
    private Date createTime;

}
