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
import org.apache.sshd.server.channel.ChannelSession;
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


}
