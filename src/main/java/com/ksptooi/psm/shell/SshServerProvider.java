package com.ksptooi.psm.shell;

import com.google.inject.Provider;
import jakarta.inject.Inject;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import java.io.IOException;

public class SshServerProvider implements Provider<SshServer> {


    @Inject
    private PSMShellFactory psmShellFactory;

    @Inject
    private SimplePasswordAuthenticator authenticator;


    @Override
    public SshServer get() {

        try {

            SshServer sshd = SshServer.setUpDefaultServer();
            sshd.setPort(1100);
            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
            sshd.setPasswordAuthenticator(authenticator);
            sshd.setShellFactory(psmShellFactory);
            sshd.start();

            return sshd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
