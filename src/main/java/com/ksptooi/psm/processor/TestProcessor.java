package com.ksptooi.psm.processor;

import com.ksptooi.uac.core.annatatiotion.Param;

import java.io.PrintWriter;

@RequestProcessor("TestProcessor")
public class TestProcessor {


    @OnActivated
    public void activated() {
        System.out.println("处理器激活回调");
    }

    @RequestHandler("*")
    public void newRequest(ProcRequest req) {

    }

    @RequestHandler("test")
    public void testHandler(ProcRequest req){
        PrintWriter w = req.getPw();
        w.println("执行无参Handler");
        w.flush();
    }

    @RequestHandler("test")
    public void testHandler(@Param("p1")String p1,ProcRequest req){
        PrintWriter w = req.getPw();
        w.println("执行有参Handler 参数为:"+p1);
        w.flush();
    }

}
