package com.ksptooi.psm.shell;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.Application;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import java.io.IOException;

@Unit
public class PSMShellFactory implements ShellFactory {



    @Override
    public Command createShell(ChannelSession session) throws IOException {
        PSMShell psmShell = new PSMShell();
        Application.getInjector().injectMembers(psmShell);
        return psmShell;
    }




}
