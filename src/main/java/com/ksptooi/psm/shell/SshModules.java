package com.ksptooi.psm.shell;

import com.google.inject.*;
import org.apache.sshd.server.SshServer;


public class SshModules extends AbstractModule {


    @Override
    protected void configure() {
        bind(PSMShellFactory.class).in(Scopes.SINGLETON);
        bind(SshServer.class).toProvider(SshServerProvider.class).asEagerSingleton();
    }


}