package com.ksptooi.inner;

import com.ksptooi.psm.mapper.StatementHistoryMapper;
import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.StatementHistoryVo;
import com.ksptooi.psm.modes.UserVo;
import com.ksptooi.psm.processor.RequestProcessor;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.processor.hook.EventHandler;
import com.ksptooi.psm.shell.ShellInstance;
import jakarta.inject.Inject;
import org.apache.commons.codec.binary.Hex;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Date;

@RequestProcessor("bundled:proc:user_statement")
public class UserStatementProcessor {

    @Inject
    private StatementHistoryMapper statementHistMapper;

    @Inject
    private UsersMapper usersMapper;

    @Inject
    private Snowflake snowflake;

    @EventHandler
    public void saveUserStatement(StatementCommitEvent event){

        final ShellInstance u = event.getUser();
        final String account = u.getSession().getSession().getUsername();
        final UserVo userVo = usersMapper.getByAccount(account);
        final String statement = event.getStatement();

        StatementHistoryVo i = new StatementHistoryVo();
        i.setId(snowflake.nextId());
        i.setUserId(userVo.getUid());
        i.setUserAccount(userVo.getAccount());
        i.setStatement(statement);
        i.setSessionId(Hex.encodeHexString(u.getSession().getSession().getSessionId()));
        i.setIpAddress(u.getSession().getSession().getClientAddress().toString());
        i.setAtTime(new Date());
        statementHistMapper.insert(i);
    }


}
