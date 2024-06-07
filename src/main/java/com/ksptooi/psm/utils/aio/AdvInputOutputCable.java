package com.ksptooi.psm.utils.aio;

import com.ksptooi.psm.vk.BufferedAndMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 高级IO线缆
 */
public class AdvInputOutputCable extends BufferedAndMatcher {

    private final long id;
    private AdvancedInputOutputPort port;

    private final Queue<char[]> is = new ArrayBlockingQueue<>(8192);
    private final Queue<String> os = new ArrayBlockingQueue<>(8192);

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

    public void flush(){

        //检查是否连接到了Port
        if(!isConnect(ConnectMode.OUTPUT)){
            return;
        }

        port.flush(this);
    }

    public void read() throws IOException {
        rl = port.read(this,rb);
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

    
    public AdvInputOutputCable nextLine() {
        os.add("\r\n");
        return this;
    }

    
    public AdvInputOutputCable print(String a) {
        os.add(a);
        return this;
    }

    
    public AdvInputOutputCable print(int i) {
        os.add(i+"");
        return this;
    }

    
    public AdvInputOutputCable println(String i) {
        os.add(i);
        nextLine();
        return this;
    }

    
    public AdvInputOutputCable println(int i) {
        os.add(i+"");
        nextLine();
        return this;
    }



    
    public long getId() {
        return id;
    }

    
    public AdvancedInputOutputPort getPort() {
        return port;
    }

    
    public boolean isConnect(ConnectMode t) {
        return port.isConnect(this, t);
    }

    public int getReadLen(){
        return rl;
    }
    public char[] getReadChars(){
        return rb;
    }
    public String getReadString(){
        return String.valueOf(rb,0,rl);
    }
}
