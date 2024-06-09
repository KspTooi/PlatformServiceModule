package com.ksptooi.psm.configset;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class ConfigSetVo {

    //设定集ID
    private Long id;

    //设定集KEY
    private String key;

    //当前值
    private String val;

    //缺省值
    private String def;//所属用户ID

    private Long userId;

    //注释
    private String description;

    //创建日期
    private Date createTime;

}
