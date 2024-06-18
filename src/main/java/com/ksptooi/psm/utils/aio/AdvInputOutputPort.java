package com.ksptooi.psm.utils.aio;

import com.ksptooi.psm.utils.RefTools;
import org.apache.sshd.server.Environment;
import xyz.downgoon.snowflake.Snowflake;

import java.io.*;
import java.util.Queue;
import java.util.UUID;

/**
 * 高级IO端口
 */
public class AdvInputOutputPort implements AdvancedInputOutputPort{

    private final String portId = UUID.randomUUID().toString();

    private final InputStream is;
    private final OutputStream os;
    private final Environment env;

    private final BufferedReader br;
    private final PrintWriter p;

    private Queue<char[]> cableInput;
    private Queue<String> cableOutput;
    private volatile AdvInputOutputCable inputCable;
    private volatile AdvInputOutputCable outputCable;

    public AdvInputOutputPort(InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
        this.p = new PrintWriter(os);
        this.br = new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public int read(AdvInputOutputCable cable,char[] c) throws IOException {

        if(! isConnect(cable,ConnectMode.INPUT)){
            throw new RuntimeException("cannot read. the cable is not connected to the input on this port");
        }

        int read = br.read(c);

        if(read == -1){
            throw new IOException();
        }

        return read;
    }

    @Override
    public void flush(AdvInputOutputCable cable) {
        if(! isConnect(cable,ConnectMode.OUTPUT)){
            throw new RuntimeException("cannot flush. the cable is not connected to the output on this port");
        }
        for(var itr = cableOutput.iterator();itr.hasNext();){
            p.print(itr.next());
        }
        p.flush();
        cableOutput.clear();
    }

    @Override
    public boolean isOnline() {
        return true;
    }


    public AdvInputOutputCable createCable(){
        return new AdvInputOutputCable(UUID.randomUUID().toString(), this);
    }

    @Override
    public void connect(AdvInputOutputCable cable, ConnectMode type, Queue<char[]> is, Queue<String> os) {

        ensureBindThisPort(cable);

        if(type.val() == ConnectMode.INPUT.val()){
            inputCable = cable;
            this.cableInput = is;
        }
        if(type.val() == ConnectMode.OUTPUT.val()){
            outputCable = cable;
            this.cableOutput = os;
            flush(cable);
        }
    }

    @Override
    public void disconnect(ConnectMode type) {
        if(type.val() == ConnectMode.INPUT.val()){
            inputCable = null;
            cableInput = null;
        }
        if(type.val() == ConnectMode.OUTPUT.val()){
            outputCable = null;
            cableOutput = null;
        }
    }

    @Override
    public boolean isConnect(AdvInputOutputCable cable, ConnectMode t) {

        ensureBindThisPort(cable);

        if(t.val() == ConnectMode.INPUT.val()){
            if(inputCable == null){
                return false;
            }
            return inputCable.getId().equals(cable.getId());
        }

        if(t.val() == ConnectMode.OUTPUT.val()){
            if(outputCable == null){
                return false;
            }
            return outputCable.getId().equals(cable.getId());
        }

        return false;
    }

    @Override
    public String getCurrentCableId(ConnectMode t) {

        if(t.val() == ConnectMode.INPUT.val()){
            return inputCable == null ? null : inputCable.getId();
        }

        if(t.val() == ConnectMode.OUTPUT.val()){
            return outputCable == null ? null : outputCable.getId();
        }

        return null;
    }

    private void ensureBindThisPort(AdvInputOutputCable cable){
        if(!cable.getPort().equals(this)){
            throw new RuntimeException("Cable没有绑定到当前的Port");
        }
    }


    public Environment getEnv(){
        return env;
    }

    @Override
    public String getPortId(){
        return this.portId;
    }

}
