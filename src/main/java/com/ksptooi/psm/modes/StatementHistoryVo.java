package com.ksptooi.psm.modes;

import lombok.Data;

import java.util.Date;

@Data
public class StatementHistoryVo {

    private Long id;
    private Long userId;
    private String statement;
    private String sessionId;
    private String ipAddress;
    private Date atTime;

}
