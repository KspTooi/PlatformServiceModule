package com.ksptooi.psm.utils.aio;

public interface AdvancedInputOutputCable {

    public long getId();

    public AdvancedInputOutputPort getPort();

    public boolean isConnect(ConnectMode t);

}
