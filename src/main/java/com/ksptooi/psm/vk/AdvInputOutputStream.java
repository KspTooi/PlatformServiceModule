package com.ksptooi.psm.vk;

import lombok.SneakyThrows;
import org.apache.sshd.server.Environment;
import xyz.downgoon.snowflake.Snowflake;

import java.io.*;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程独占的IO
 */
public class AdvInputOutputStream extends BufferedAndMatcher{

    private static final Snowflake snowflake = new Snowflake(1,30);

    private final InputStream is;
    private final OutputStream os;
    private final Environment env;

    private final BufferedReader b;
    private final PrintWriter p;

    private final Map<Long, ForwardStream> subStreamMap = new ConcurrentHashMap<>();

    /**
     * SubStreams
     */
    private final Long subStreamId;
    private AdvInputOutputStream parent = null;
    private Queue<String> subOs;

    //当前独占的out 和 in
    private long subStreamInput = -1;
    private long subStreamOutput = -1;

    public AdvInputOutputStream(InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
        this.b = new BufferedReader(new InputStreamReader(this.is));
        this.p = new PrintWriter(this.os);
        this.subStreamId = -1L;
    }

    public AdvInputOutputStream(Long id, AdvInputOutputStream parent, InputStream is, Queue<String> os, Environment env){
        this.is = is;
        this.os = null;
        this.env = env;
        this.b = new BufferedReader(new InputStreamReader(is));
        this.subOs = os;
        this.p = null;
        this.subStreamId = id;
        this.parent = parent;
    }


    public AdvInputOutputStream createSubStream(){
        final long id = snowflake.nextId();
        final ForwardStream subForwardStream = new ForwardStream(id, this, env);
        subStreamMap.put(id,subForwardStream);
        return subForwardStream.getInstance();
    }

    @SneakyThrows
    public void destroySubStream(Long id){

        if(!subStreamMap.containsKey(id)){
            return;
        }

        ForwardStream fs = subStreamMap.get(id);
        fs.destroy();
        subStreamMap.remove(id);
    }

    public void read() throws IOException {

        if(isSubStream()){
            if(!parent.isHeldInput(subStreamId)){
                throw new NotAttachedException("The SubStream is not attached to the InputStream");
            }
        }

        rl = b.read(rb);

        //转发到subStream
        if(subStreamInput != -1){
            subStreamMap.get(subStreamInput).getForwardCharOut().write(rb,0,rl);
            subStreamMap.get(subStreamInput).getForwardCharOut().flush();
            rl = 0;
            return;
        }

        if(rl < 1){
            throw new IOException();
        }
    }



    public AdvInputOutputStream print(String a){

        if(isSubStream()){
            subOs.add(a);
            return this;
        }

        p.print(a);
        return this;
    }

    public AdvInputOutputStream println(String a){
        if(isSubStream()){
            subOs.add(a);
            subOs.add("\r\n");
            return this;
        }
        p.print(a);
        return this;
    }
    public AdvInputOutputStream println(int i){
        return println(i+"");
    }

    public AdvInputOutputStream flush(){

        if(isSubStream()){
            parent.notifyFlush(this.subStreamId);
            return this;
        }

        p.flush();
        return this;
    }

    /**
     * 子AIOS通知父AIOS
     */
    @SneakyThrows
    private void notifyFlush(long subStreamId){

        ensureNotSubStream();

        if(!subStreamMap.containsKey(subStreamId)){
            return;
        }


        Queue<String> forwardIn = subStreamMap.get(subStreamId).getForwardIn();

        if(subStreamOutput != subStreamId){
            forwardIn.clear();
            return;
        }

        if(!forwardIn.isEmpty()){
            assert p != null;
            forwardIn.forEach(p::print);
            p.flush();
            forwardIn.clear();
        }

    }

    public int directRead(char[] c) throws IOException {
        return b.read(c);
    }

    public int directRead() throws IOException{
        return b.read();
    }

    public int getReadLen(){
        return rl;
    }
    public char[] getReadChars(){
        return rb;
    }

    public Long getId() {
        return subStreamId;
    }

    public boolean isSubStream(){
        return parent != null;
    }


    public boolean isHeldInput(long subStreamId){
        if(isSubStream()){
            return parent.isHeldInput(subStreamId);
        }
        return subStreamInput == subStreamId;
    }
    public boolean isHeldOutput(long subStreamId){
        if(isSubStream()){
            return parent.isHeldOutput(subStreamId);
        }
        return subStreamOutput == subStreamId;
    }

    public void attachInput(){
        ensureIsSubStream();
        parent.notifyAttachInput(subStreamId);
    }
    public void attachOutput(){

        //顶层AIO解除子IO的Attach
        if(!isSubStream()){
            ensureNotSubStream();
            subStreamOutput = -1;
            return;
        }

        ensureIsSubStream();
        parent.notifyAttachOutput(subStreamId);
    }
    public void detachInput(){

        //顶层AIO解除子IO的Attach
        if(!isSubStream()){
            ensureNotSubStream();
            subStreamInput = -1;
            return;
        }

        ensureIsSubStream();
        parent.notifyDetachInput(subStreamId);
    }
    public void detachOutput(){
        ensureIsSubStream();
        parent.notifyDetachOutput(subStreamId);
    }

    @SneakyThrows
    public void destroy(){

        if(isSubStream()){
            parent.destroySubStream(subStreamId);
            return;
        }

        detachInput();
        detachOutput();
        is.close();
        os.close();
    }


    private void notifyAttachInput(long subStreamId){
        ensureNotSubStream();
        subStreamInput = subStreamId;
    }
    private void notifyAttachOutput(long subStreamId){
        ensureNotSubStream();
        subStreamOutput = subStreamId;
    }
    private void notifyDetachInput(long subStreamId){
        ensureNotSubStream();
        if(!isHeldInput(subStreamId)){
            return;
        }
        subStreamInput = -1;
    }
    private void notifyDetachOutput(long subStreamId){
        ensureNotSubStream();
        if(!isHeldOutput(subStreamId)){
            return;
        }
        subStreamOutput = -1;
    }

    private void ensureIsSubStream(){
        if(!isSubStream()){
            throw new NotSupportOperationException("the TopLayerStream not support this operation");
        }
    }

    private void ensureNotSubStream(){
        if(isSubStream()){
            throw new NotSupportOperationException("the subStream not support this operation");
        }
    }

    public void printDebugText(){

        StringBuilder sb = new StringBuilder();

        if(!isSubStream()){
            sb.append("AIO->");
        }else {
            sb.append("AIO(").append(subStreamId).append(")->");
        }

        sb.append("[ ");
        for (int i = 0; i < rl; i++) {
            sb.append((int)rb[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ]");

        System.out.println("VK_PRINT:: "+sb.toString());
    }

}