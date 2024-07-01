package com.ksptooi.psm.modes;

import lombok.Data;
import java.util.Date;

@Data
public class UserVo {

    private Long uid;

    private String account;

    private String password;

    //0:正常 1:禁用 2:删除
    private Integer status;

    private Date lastLoginTime;

    private Date createTime;

}
