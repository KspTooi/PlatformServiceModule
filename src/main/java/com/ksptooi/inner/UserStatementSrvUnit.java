package com.ksptooi.inner;

import com.ksptooi.psm.mapper.StatementHistoryMapper;
import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.StatementHistoryVo;
import com.ksptooi.psm.modes.UserVo;
import com.ksptooi.psm.processor.EventHandler;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.processor.event.BadRequestEvent;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.shell.Colors;
import jakarta.inject.Inject;
import org.apache.commons.codec.binary.Hex;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Date;

@ServiceUnit("bundled:UserStatementSrvUnit")
public class UserStatementSrvUnit {

    @Inject
    private StatementHistoryMapper statementHistMapper;

    @Inject
    private UsersMapper usersMapper;

    @Inject
    private Snowflake snowflake;


    @EventHandler(global = true)
    public void saveUserStatement(StatementCommitEvent event){

        var session = event.getUserShell().getSession().getSession();
        var sessionId = session.getSessionId();
        var account = session.getUsername();

        final UserVo userVo = usersMapper.getByAccount(account);
        final var statement = event.getStatement();

        StatementHistoryVo i = new StatementHistoryVo();
        i.setId(snowflake.nextId());
        i.setUserId(userVo.getUid());
        i.setUserAccount(userVo.getAccount());
        i.setStatement(statement);
        i.setSessionId(Hex.encodeHexString(sessionId));
        i.setIpAddress(session.getClientAddress().toString());
        i.setAtTime(new Date());
        statementHistMapper.insert(i);
    }


    @EventHandler(global = true)
    public void badRequestNotify(BadRequestEvent event){

        var request = event.getRequest();
        var w = request.getShell().getCable();

        if(event.getErrorCode().equals(BadRequestEvent.ERR_INVOKE_EXCEPTION)){
            return;
        }

        if(event.getErrorCode().equals(BadRequestEvent.ERR_HANDLER_TYPE_INCONSISTENT)){
            w.print("fatal: ");
            w.print(request.getPattern());
            w.print(" ");
            w.print("无法处理请求,处理器与标定的不一致.");
            w.flush();
            return;
        }

        w.print(Colors.RED);
        w.print(request.getPattern());
        w.print(": 无法处理请求,匹配处理器失败.");
        w.print(Colors.RESET);
        w.flush();
    }


}
