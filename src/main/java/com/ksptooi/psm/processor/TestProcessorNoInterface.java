package com.ksptooi.psm.processor;

import com.ksptooi.uac.core.annatatiotion.Param;

import java.util.List;

@RequestProcessor("TestProcessor")
public class TestProcessorNoInterface{

    @RequestHandler("*")
    public void newRequest(ProcRequest request){

        List<String> parameter = request.getParameter();

    }

    @OnActivated
    public void start(){

    }

    @OnDestroy
    public void close(){

    }

    @RequestHandler("ls")
    public void listFiles(){
    }

    @RequestHandler("ls")
    public void listFiles(@Param("directory")String dir,ProcRequest request){


    }

}
