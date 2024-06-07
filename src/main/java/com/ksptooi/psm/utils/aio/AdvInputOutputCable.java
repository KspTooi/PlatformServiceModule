package com.ksptooi.psm.utils.aio;

import com.ksptooi.psm.vk.BufferedAndMatcher;

import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 高级IO线缆
 */
public class AdvInputOutputCable extends BufferedAndMatcher implements AdvancedInputOutputCable {

    private final long id;
    private AdvancedInputOutputPort port;

    private Queue<char[]> is = new ArrayBlockingQueue<>(8192);
    private Queue<String> os = new ArrayBlockingQueue<>(8192);

    public AdvInputOutputCable(Long id, AdvancedInputOutputPort port){
        this.id = id;
        this.port = port;
    }

    public void connect(ConnectMode m){
        port.connect(this,m,is,os);
    }

    public void disconnect(ConnectMode m){
        port.disconnect(m);
    }

    //迁移线缆到新端口
    public void bindPort(AdvancedInputOutputPort port){

        //先断开线缆与已有端口的连接
        if(isConnect(ConnectMode.INPUT)){
            disconnect(ConnectMode.INPUT);
        }
        if(isConnect(ConnectMode.OUTPUT)){
            disconnect(ConnectMode.OUTPUT);
        }

        //更换port
        this.port = port;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public AdvancedInputOutputPort getPort() {
        return port;
    }

    @Override
    public boolean isConnect(ConnectMode t) {
        return port.isConnect(this, t);
    }
}
