package com.ksptooi.psm.vk;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.sshd.server.Environment;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

@Getter
public class ForwardStream {

    private final PipedInputStream forwardIn;
    private final PipedOutputStream forwardOut;
    private final PrintWriter forwardPwOut;
    private final AdvInputOutputStream instance;

    @SneakyThrows
    public ForwardStream(Long id,AdvInputOutputStream parent, Environment env) {

        final PipedInputStream subIn = new PipedInputStream(204800);
        final PipedOutputStream subOut = new PipedOutputStream();

        this.forwardOut = new PipedOutputStream(subIn);
        this.forwardIn = new PipedInputStream(subOut,204800);
        this.forwardPwOut = new PrintWriter(forwardOut);
        instance = new AdvInputOutputStream(id,parent,subIn,subOut,env);
    }

    @SneakyThrows
    public void destroy(){
        instance.detachInput();
        instance.detachOutput();
        forwardIn.close();
        forwardOut.close();
    }

}
