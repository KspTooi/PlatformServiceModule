package com.ksptooi.psm.processor;

@RequestProcessor("TestProcessor")
public class TestProcessor implements Processor {

    @Override
    public void activated() {
        System.out.println("处理器激活回调");
    }

    @Override
    public void newRequest(ProcRequest req, ProcResponse res) {

    }

    @Override
    public void destroy() {

    }

}
