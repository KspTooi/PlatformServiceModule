package com.ksptooi.psm.utils.aio;

import org.apache.sshd.server.Environment;
import xyz.downgoon.snowflake.Snowflake;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;

/**
 * 高级IO端口
 */
public class AdvInputOutputPort implements AdvancedInputOutputPort{

    private final Snowflake snowflake = new Snowflake(2,16);

    private final InputStream is;
    private final OutputStream os;
    private final Environment env;

    private Queue<char[]> cableInput;
    private Queue<String> cableOutput;
    private volatile AdvancedInputOutputCable inputCable;
    private volatile AdvancedInputOutputCable outputCable;

    public AdvInputOutputPort(InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
    }

    @Override
    public void read() {

    }

    @Override
    public void flush() {

    }


    @Override
    public boolean isOnline() {
        return false;
    }


    public AdvancedInputOutputCable createCable(){
        return new AdvInputOutputCable(snowflake.nextId(), this);
    }

    @Override
    public void connect(AdvancedInputOutputCable cable, ConnectMode type, Queue<char[]> is, Queue<String> os) {

        ensureBindThisPort(cable);

        if(type.val() == ConnectMode.INPUT.val()){
            inputCable = cable;
            this.cableInput = is;
        }
        if(type.val() == ConnectMode.OUTPUT.val()){
            outputCable = cable;
            this.cableOutput = os;
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
    public boolean isConnect(AdvancedInputOutputCable cable, ConnectMode t) {

        ensureBindThisPort(cable);

        if(t.val() == ConnectMode.INPUT.val()){
            if(inputCable == null){
                return false;
            }
            return inputCable.getId() == cable.getId();
        }

        if(t.val() == ConnectMode.OUTPUT.val()){
            if(outputCable == null){
                return false;
            }
            return outputCable.getId() == cable.getId();
        }

        return false;
    }

    @Override
    public long getCurrentCableId(ConnectMode t) {

        if(t.val() == ConnectMode.INPUT.val()){
            return inputCable == null ? -1 : inputCable.getId();
        }

        if(t.val() == ConnectMode.OUTPUT.val()){
            return outputCable == null ? -1 : outputCable.getId();
        }

        return -1;
    }

    private void ensureBindThisPort(AdvancedInputOutputCable cable){
        if(!cable.getPort().equals(this)){
            throw new RuntimeException("Cable没有绑定到当前的Port");
        }
    }

}
