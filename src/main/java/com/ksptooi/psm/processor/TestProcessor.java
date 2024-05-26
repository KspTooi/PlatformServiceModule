package com.ksptooi.psm.processor;

import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.entities.CliCommand;

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

    @RequestName("install")
    @Alias({"i"})
    public void appInstall(@Param("appName")String appName,@Param("path")String path, ProcRequest req){

        

    }

}
