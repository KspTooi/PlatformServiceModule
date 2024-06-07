package com.ksptooi.psm.utils.aio;

import java.util.Queue;

/**
 * 高级IO端口
 */
public interface AdvancedInputOutputPort {

    public void read();

    public void flush();

    public boolean isOnline();

    public AdvancedInputOutputCable createCable();

    public void connect(AdvancedInputOutputCable cable, ConnectMode type, Queue<char[]> is, Queue<String> os);

    public void disconnect(ConnectMode type);

    public boolean isConnect(AdvancedInputOutputCable cable,ConnectMode t);

    public long getCurrentCableId(ConnectMode t);
}
