package com.ksptooi.psm.utils.aio;

import java.io.IOException;
import java.util.Queue;

/**
 * 高级IO端口
 */
public interface AdvancedInputOutputPort {

    public int read(AdvInputOutputCable cable,char[] c) throws IOException;

    public void flush(AdvInputOutputCable cable);

    public boolean isOnline();

    public AdvInputOutputCable createCable();

    public void connect(AdvInputOutputCable cable, ConnectMode type, Queue<char[]> is, Queue<String> os);

    public void disconnect(ConnectMode type);

    public boolean isConnect(AdvInputOutputCable cable,ConnectMode t);

    public long getCurrentCableId(ConnectMode t);
}
