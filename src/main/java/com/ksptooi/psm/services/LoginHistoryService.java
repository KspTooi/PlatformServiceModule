package com.ksptooi.psm.services;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.LoginHistoryMapper;
import com.ksptooi.psm.modes.LoginHistoryVo;
import jakarta.inject.Inject;
import xyz.downgoon.snowflake.Snowflake;
import java.net.SocketAddress;
import java.util.Date;

@Unit
public class LoginHistoryService {

    @Inject
    private LoginHistoryMapper mapper;

    @Inject
    private Snowflake snowflake;

    public void newLoginSuccessRecord(String account,SocketAddress origin){
        LoginHistoryVo i = new LoginHistoryVo();
        i.setId(snowflake.nextId());
        i.setAccount(account);
        i.setOperate("login");
        i.setResult("success");
        i.setReason("-");
        i.setAtTime(new Date());
        i.setOrigin(origin.toString());
        mapper.insert(i);
    }

    public void newLoginFailedRecord(String account, SocketAddress origin, String reason){
        LoginHistoryVo i = new LoginHistoryVo();
        i.setId(snowflake.nextId());
        i.setAccount(account);
        i.setOperate("login");
        i.setResult("failed");
        i.setReason(reason);
        i.setAtTime(new Date());
        i.setOrigin(origin.toString());
        mapper.insert(i);
    }

}
