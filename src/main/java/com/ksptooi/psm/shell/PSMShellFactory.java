package com.ksptooi.psm.shell;

import com.ksptooi.guice.annotations.Unit;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.IOException;

@Unit
public class PSMShellFactory implements ShellFactory {


    @Override
    public Command createShell(ChannelSession channelSession) throws IOException {
        return null;
    }


}
