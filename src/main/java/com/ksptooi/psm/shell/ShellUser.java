package com.ksptooi.psm.shell;

import lombok.Getter;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

@Getter
public class ShellUser {

    private final ExitCallback exitCallback;
    private final OutputStream eos;
    private final OutputStream os;
    private final PrintWriter pw;
    private final InputStream is;
    private final ChannelSession session;
    private final Environment env;

    public ShellUser(ExitCallback ecb,OutputStream eos,OutputStream os,PrintWriter pw,InputStream is,ChannelSession session,Environment env){
        this.exitCallback = ecb;
        this.eos = eos;
        this.os = os;
        this.pw = pw;
        this.is = is;
        this.session = session;
        this.env = env;
    }

}
