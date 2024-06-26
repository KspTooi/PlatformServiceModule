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
        var cable = request.getShell().getCable();

        if(event.getErrorCode().equals(BadRequestEvent.ERR_INVOKE_EXCEPTION)){
            return;
        }

        if(event.getErrorCode().equals(BadRequestEvent.ERR_HANDLER_TYPE_INCONSISTENT)){

            cable.w("fatal: ").w(request.getPattern()).w("无法处理请求,处理器与标定的不一致.");
            cable.nextLine();
            return;
        }

        cable.w(Colors.RED).w(request.getPattern()).w(": 不是内部或外部命令,也不是可运行的程序或服务.").w(Colors.RESET);
        cable.nextLine();
    }

}
