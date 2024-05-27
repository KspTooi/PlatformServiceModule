package com.ksptooi.psm.processor;

import com.ksptooi.uac.core.annatatiotion.Param;

@RequestProcessor("TestProcessor")
public class TestProcessor implements Processor {


    @Override
    public void activated() {
        System.out.println("处理器激活回调");
    }

    @Override
    public void newRequest(ProcRequest req) {

    }

    @Override
    public void destroy() {
    }

    @RequestName("ls")
    public void listFiles(ProcRequest req){

    }

    @RequestName("ls")
    @Alias({"Ls","lS"})
    public void listFiles(@Param("p1")String p1,@Param("p2")String p2, ProcRequest req){
        String username = req.getSession().getSession().getUsername();
        //获取用户当前目录
    }

}
