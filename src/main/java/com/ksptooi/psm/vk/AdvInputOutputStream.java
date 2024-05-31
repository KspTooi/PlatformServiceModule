package com.ksptooi.psm.vk;

import org.apache.sshd.server.Environment;
import xyz.downgoon.snowflake.Snowflake;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程独占的IO
 */
public class AdvInputOutputStream extends BufferedAndMatcher{

    private static final Snowflake snowflake = new Snowflake(1,50);

    private final InputStream is;
    private final OutputStream os;
    private final Environment env;

    private final BufferedReader b;
    private final PrintWriter p;

    private boolean reading = false;

    /**
     * SubStreams
     */
    private final Long subStreamId;
    private Map<Long, AdvInputOutputStream> subStreams = new HashMap<>();
    private AdvInputOutputStream parent = null;

    //当前独占的out 和 in
    private AdvInputOutputStream stickyOut = null;
    private AdvInputOutputStream stickyIn = null;

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
        this.reading = false;
        this.parent = parent;
    }


    public AdvInputOutputStream createSub(){

        final long id = snowflake.nextId();

        final ForwardStream forwardStream = new ForwardStream(id, this, env);

        AdvInputOutputStream subAIOS = forwardStream.getInstance();

        return null;
    }

    public void removeSub(Long id){

    }

    public void read() throws IOException {
        rl = b.read(rb);
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

            return this;
        }

        p.flush();
        return this;
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
}