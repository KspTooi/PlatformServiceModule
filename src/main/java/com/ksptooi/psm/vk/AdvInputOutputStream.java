package com.ksptooi.psm.vk;

import lombok.SneakyThrows;
import org.apache.sshd.server.Environment;
import xyz.downgoon.snowflake.Snowflake;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * SubStreams
     */
    private final Long subStreamId;
    private Map<Long, ForwardStream> subStreamMap = new ConcurrentHashMap<>();
    private AdvInputOutputStream parent = null;

    //当前独占的out 和 in
    private long stickyOutId = -1;
    private long stickyInId = -1;

    public AdvInputOutputStream(InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
        this.b = new BufferedReader(new InputStreamReader(this.is));
        this.p = new PrintWriter(this.os);
        this.subStreamId = -1L;
    }

    public AdvInputOutputStream(Long id,AdvInputOutputStream parent, InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
        this.b = new BufferedReader(new InputStreamReader(is));
        this.p = new PrintWriter(os);
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

        rl = b.read(rb);

        //转发到subStream
        if(stickyOutId != -1){
            subStreamMap.get(stickyOutId).getForwardPwOut().write(rb,0,rl);
            subStreamMap.get(stickyOutId).getForwardPwOut().flush();
            rl = 0;
            return;
        }

        if(rl < 1){
            throw new IOException();
        }
    }

    public AdvInputOutputStream print(String a){
        p.print(a);
        return this;
    }

    public AdvInputOutputStream println(String a){
        p.print(a);
        return this;
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

        if(!subStreamMap.containsKey(subStreamId)){
            return;
        }

        if(stickyInId != subStreamId){
            subStreamMap.get(subStreamId).getForwardIn().reset();
        }

        subStreamMap.get(subStreamId).getForwardIn().transferTo(os);
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


    private boolean isHeldInBySubStreamId(long subStreamId){
        return stickyInId == subStreamId;
    }
    private boolean isHeldOutBySubStreamId(long subStreamId){
        return stickyOutId == subStreamId;
    }

    public void attachInput(){
        if(!isSubStream()){
            return;
        }
        parent.notifyAttachInput(subStreamId);
    }
    public void attachOutput(){
        if(!isSubStream()){
            return;
        }
        parent.notifyAttachOutput(subStreamId);
    }
    public void detachInput(){
        if(!isSubStream()){
            return;
        }
        parent.notifyDetachInput(subStreamId);
    }
    public void detachOutput(){
        if(!isSubStream()){
            return;
        }
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
        stickyInId = subStreamId;
    }
    private void notifyAttachOutput(long subStreamId){
        stickyOutId = subStreamId;
    }
    private void notifyDetachInput(long subStreamId){
        if(!isHeldInBySubStreamId(subStreamId)){
            return;
        }
        stickyInId = -1;
    }
    private void notifyDetachOutput(long subStreamId){
        if(!isHeldOutBySubStreamId(subStreamId)){
            return;
        }
        stickyOutId = -1;
    }

}