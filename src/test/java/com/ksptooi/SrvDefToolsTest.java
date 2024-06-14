package com.ksptooi;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.processor.entity.SrvDefine;
import com.ksptooi.psm.processor.OnActivated;
import com.ksptooi.psm.processor.OnDestroyed;
import com.ksptooi.uac.core.annatatiotion.Param;
import org.junit.Test;

import java.util.List;

public class SrvDefToolsTest {

    public static void main(String[] args) {

        TestServiceUnit tp = new TestServiceUnit();

        //List<SrvDefine> srvDefine = SrvUnitTools.getRequestDefine(TestServiceUnit.class);

        //System.out.println(srvDefine);

    }

    @Test
    public void getSrvDefine() throws SrvDefineException {

        List<SrvDefine> srvDefine = SrvDefTools.getSrvDefine(TestProc.class);

        System.out.println(srvDefine);

    }

}

class TestProc{

    @RequestHandler("*")
    public void newRequest(ShellRequest request){
    }

    @OnActivated
    public void start(){

    }

    @OnDestroyed
    public void close(){

    }

    @RequestHandler("ls")
    public void listFiles(){
    }

    @RequestHandler("ls")
    public void listFiles(@Param("directory")String dir, ShellRequest request){
    }

}
