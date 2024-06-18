package com.ksptooi.psm.utils.aio;

import asia.kala.ansi.AnsiString;
import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.utils.aio.color.CableDye;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 高级IO线缆
 */
public class AdvInputOutputCable extends BufferedAndMatcher {

    private final String id;
    private AdvancedInputOutputPort port;

    private boolean destroyed = false;

    private final Queue<char[]> is = new ArrayBlockingQueue<>(8192);
    private final Queue<String> os = new ArrayBlockingQueue<>(8192);

    //线缆染料
    private volatile CableDye dye = null;

    private volatile Map<String,CableStateListener> listener;

    public AdvInputOutputCable(String id, AdvancedInputOutputPort port){
        this.id = id;
        this.port = port;
    }
    
    public AdvInputOutputCable connect(ConnectMode m){
        ensureCableNotDestroyed();

        if(!isConnect(m)){
            port.connect(this,m,is,os);
            triggerUpdate();
        }

        return this;
    }

    public AdvInputOutputCable connect(){
        connect(ConnectMode.INPUT);
        connect(ConnectMode.OUTPUT);
        return this;
    }

    public AdvInputOutputCable disconnect(ConnectMode m){
        if(isConnect(m)){
            port.disconnect(m);
            triggerUpdate();
        }
        return this;
    }
    public AdvInputOutputCable disconnect(){
        disconnect(ConnectMode.INPUT);
        disconnect(ConnectMode.OUTPUT);
        return this;
    }

    public void read() throws IOException {
        ensureCableNotDestroyed();
        rl = port.read(this,rb);
    }

    //迁移线缆到新端口
    public AdvInputOutputCable bindPort(AdvancedInputOutputPort port){
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
        triggerUpdate();
        return this;
    }

    public AdvInputOutputCable flush(){

        ensureCableNotDestroyed();

        //检查是否连接到了Port
        if(!isConnect(ConnectMode.OUTPUT)){
            return this;
        }
        wash();
        port.flush(this);
        return this;
    }

    public AdvInputOutputCable nextLine() {
        ensureCableNotDestroyed();
        os.add("\r\n");
        return this;
    }

    public AdvInputOutputCable print(String a) {
        ensureCableNotDestroyed();
        if(dye == null){
            os.add(a);
        }else {
            os.add(AnsiString.Color.True(dye.r(),dye.g(),dye.b()).overlay(a).toString());
        }
        return this;
    }
    public AdvInputOutputCable print(byte b){
        return print(String.valueOf(b));
    }
    public AdvInputOutputCable print(short s){
        return print(String.valueOf(s));
    }
    public AdvInputOutputCable print(int i) {
        return print(String.valueOf(i));
    }
    public AdvInputOutputCable print(long l){
        return print(String.valueOf(l));
    }
    public AdvInputOutputCable print(float f){
        return print(String.valueOf(f));
    }
    public AdvInputOutputCable print(double d){
        return print(String.valueOf(d));
    }
    public AdvInputOutputCable print(boolean b){
        return print(String.valueOf(b));
    }
    public AdvInputOutputCable print(char c){
        return print(String.valueOf(c));
    }
    public AdvInputOutputCable print(char[] c){
        return print(String.valueOf(c));
    }
    public AdvInputOutputCable print(Object o){
        return print(o.toString());
    }

    public AdvInputOutputCable println(String i) {
        ensureCableNotDestroyed();
        print(i).nextLine();
        return this;
    }
    public AdvInputOutputCable println(byte v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(short v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(int v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(long v) {
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(float v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(double v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(boolean v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(char v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(char[] v){
        return println(String.valueOf(v));
    }
    public AdvInputOutputCable println(Object v){
        return println(v.toString());
    }
    public AdvInputOutputCable w(byte v){
        return print(v);
    }
    public AdvInputOutputCable w(short v){
        return print(v);
    }
    public AdvInputOutputCable w(int v) {
        return print(v);
    }
    public AdvInputOutputCable w(long v){
        return print(v);
    }
    public AdvInputOutputCable w(float v){
        return print(v);
    }
    public AdvInputOutputCable w(double v){
        return print(v);
    }
    public AdvInputOutputCable w(boolean v){
        return print(v);
    }
    public AdvInputOutputCable w(char v){
        return print(v);
    }
    public AdvInputOutputCable w(char[] v){
        return print(v);
    }
    public AdvInputOutputCable w(Object v){
        return print(v);
    }

    public synchronized AdvInputOutputCable dye(CableDye dye){
        this.dye = dye;
        return this;
    }

    public synchronized AdvInputOutputCable wash(){
        this.dye = null;
        return this;
    }

    public String getId() {
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
        triggerUpdate();
    }

    /**
     * 检测并保证Cable没有被销毁
     */
    public void ensureCableNotDestroyed(){
        if(port == null || destroyed){
            throw new RuntimeException("this cable unavailable");
        }
    }

    public void triggerUpdate(){
        listener.values().forEach((ret)-> ret.update(this));
    }
    public void addStateListener(String k, CableStateListener csl){
        listener.put(k,csl);
    }
    public void removeStateListener(String k){
        listener.remove(k);
    }

}
