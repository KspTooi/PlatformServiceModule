package com.ksptooi.psm.processor.event;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.channel.ChannelSession;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class CancellableEvent implements ProcEvent{

    /**
     * 当事件被取消 发布者不会继续执行其他动作
     */
    private boolean isCanceled = false;

    /**
     * 当事件被拦截后 不会再继续传递给下一个事件处理器执行
     */
    private boolean isIntercepted = false;

    @Getter@Setter
    private OutputStream eos;
    @Getter@Setter
    private OutputStream os;
    @Getter@Setter
    private PrintWriter pw;
    @Getter@Setter
    private InputStream is;
    @Getter@Setter
    private ChannelSession session;
    @Getter@Setter
    private Environment env;

    public void cancel(){
        isCanceled = true;
    }

    public void intercept(){
        isIntercepted = true;
    }

    public boolean isCanceled(){
        return isCanceled;
    }
    public boolean isIntercepted(){
        return isIntercepted;
    }


}
