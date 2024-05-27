package com.ksptooi.psm.processor.event;

public abstract class CancellableEvent {

    private boolean isCancel = false;

    public void cancel(){
        isCancel = true;
    }

}
