package com.ksptooi.psm.processor;

public interface Processor {

    public void activated();

    public void newRequest(ProcRequest req);

    public void destroy();
}