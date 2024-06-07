package com.ksptooi.psm.utils.aio;

public enum ConnectMode {

    INPUT(0),
    OUTPUT(1);

    private final int val;

    ConnectMode(int val) {
        this.val = val;
    }

    public int val(){
        return val;
    }
}
