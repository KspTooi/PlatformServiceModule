package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class LoginHistoryVo {
    private Long id;
    private String account;
    private String operate;
    private String result;
    private String reason;
    private Date atTime;
}
