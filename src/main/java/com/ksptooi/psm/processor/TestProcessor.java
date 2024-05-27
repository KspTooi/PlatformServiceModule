package com.ksptooi.psm.processor;

import com.ksptooi.uac.core.annatatiotion.Param;

@RequestProcessor("TestProcessor")
public class TestProcessor {


    @OnActivated
    public void activated() {
        System.out.println("处理器激活回调");
    }

    @RequestHandler("*")
    public void newRequest(ProcRequest req) {

    }

}
