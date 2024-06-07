package com.ksptooi.psm.utils.aio;

import com.ksptooi.psm.vk.BufferedAndMatcher;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 高级IO线缆
 */
public class AdvInputOutputCable extends BufferedAndMatcher {

    private final long id;
    private AdvancedInputOutputPort port;

    private boolean destroyed = false;

    private final Queue<char[]> is = new ArrayBlockingQueue<>(8192);
    private final Queue<String> os = new ArrayBlockingQueue<>(8192);

    public AdvInputOutputCable(Long id, AdvancedInputOutputPort port){
        this.id = id;
        this.port = port;
    }
    
    public void connect(ConnectMode m){
        ensureCableNotDestroyed();

        if(!isConnect(m)){
            port.connect(this,m,is,os);
        }

    }

    public void connect(){
        connect(ConnectMode.INPUT);
        connect(ConnectMode.OUTPUT);
    }

    public void disconnect(ConnectMode m){
        if(isConnect(m)){
            port.disconnect(m);
        }
    }
    public void disconnect(){
        disconnect(ConnectMode.INPUT);
        disconnect(ConnectMode.OUTPUT);
    }

    public void flush(){

        ensureCableNotDestroyed();

        //检查是否连接到了Port
        if(!isConnect(ConnectMode.OUTPUT)){
            return;
        }

        port.flush(this);
    }

    public void read() throws IOException {
        ensureCableNotDestroyed();
        rl = port.read(this,rb);
    }

    //迁移线缆到新端口
    public void bindPort(AdvancedInputOutputPort port){

        ensureCableNotDestroyed();

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
        ensureCableNotDestroyed();
        os.add("\r\n");
        return this;
    }

    
    public AdvInputOutputCable print(String a) {
        ensureCableNotDestroyed();
        os.add(a);
        return this;
    }

    
    public AdvInputOutputCable print(int i) {
        ensureCableNotDestroyed();
        os.add(i+"");
        return this;
    }

    
    public AdvInputOutputCable println(String i) {
        ensureCableNotDestroyed();
        os.add(i);
        nextLine();
        return this;
    }

    
    public AdvInputOutputCable println(int i) {
        ensureCableNotDestroyed();
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
        return port != null && port.isConnect(this, t);
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

    public void printDebugText(){

        StringBuilder sb = new StringBuilder();

        sb.append("CABLE(").append(id).append(")->");

        sb.append("[ ");
        for (int i = 0; i < rl; i++) {
            sb.append((int)rb[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ]");

        System.out.println("VK_PRINT:: "+sb.toString());
    }

    public void destroy(){
        disconnect();
        is.clear();
        os.clear();
        port = null;
        destroyed = true;
    }

    /**
     * 检测并保证Cable没有被销毁
     */
    public void ensureCableNotDestroyed(){
        if(port == null || destroyed){
            throw new RuntimeException("this cable unavailable");
        }
    }

}
