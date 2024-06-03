package com.ksptooi.psm.vk;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.sshd.server.Environment;

import java.io.CharArrayWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class ForwardStream {


    private final PipedOutputStream forwardOut;
    private final PrintWriter forwardCharOut;
    private final Queue<String> forwardIn;
    private final AdvInputOutputStream instance;

    @SneakyThrows
    public ForwardStream(Long id,AdvInputOutputStream parent, Environment env) {
        final PipedInputStream subIn = new PipedInputStream(2048);
        this.forwardOut = new PipedOutputStream(subIn);
        this.forwardCharOut = new PrintWriter(forwardOut);
        forwardIn = new ConcurrentLinkedQueue<>();
        instance = new AdvInputOutputStream(id,parent,subIn, forwardIn,env);
    }

    @SneakyThrows
    public ForwardStream(AdvInputOutputStream subStream,AdvInputOutputStream parent, Environment env) {

        if(!subStream.isSubStream()){
            throw new RuntimeException("无法将一个顶层AIO加入到顶层AIO中.");
        }

        //重新构建流
        final var subIn = new PipedInputStream(2048);
        this.forwardOut = new PipedOutputStream(subIn);
        this.forwardCharOut = new PrintWriter(forwardOut);
        instance = subStream;
        this.forwardIn = subStream.rebuild(subIn, parent, env);
    }

    @SneakyThrows
    public void destroy(){
        instance.detachInput();
        instance.detachOutput();
        //forwardIn
        forwardOut.close();
    }

}
