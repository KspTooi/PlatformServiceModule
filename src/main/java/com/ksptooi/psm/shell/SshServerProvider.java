package com.ksptooi.psm.shell;

import com.google.inject.Provider;
import jakarta.inject.Inject;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

import java.io.IOException;

public class SshServerProvider implements Provider<SshServer> {


    @Inject
    private PSMShellFactory psmShellFactory;


    @Override
    public SshServer get() {

        try {

            SshServer sshd = SshServer.setUpDefaultServer();
            sshd.setPort(1100);
            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
            sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
                @Override
                public boolean authenticate(String username, String password, ServerSession session) {
                    return "1".equals(username) && "1".equals(password);  // 简单的用户名密码验证
                }
            });
            sshd.setShellFactory(psmShellFactory);
            sshd.start();

            return sshd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
