package com.ksptooi;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.processor.entity.ProcDefine;
import com.ksptooi.psm.processor.hook.OnActivated;
import com.ksptooi.psm.processor.hook.OnDestroy;
import com.ksptooi.uac.core.annatatiotion.Param;
import org.junit.Test;

import java.util.List;

public class ProcToolsTest {

    public static void main(String[] args) {

        TestServiceUnit tp = new TestServiceUnit();

        List<ProcDefine> procDefine = ProcTools.getRequestDefine(TestServiceUnit.class);

        System.out.println(procDefine);

    }

    @Test
    public void getProcDefine() throws ProcDefineException {

        List<ProcDefine> procDefine = ProcTools.getProcDefine(TestProc.class);

        System.out.println(procDefine);

    }

}

@RequestProcessor("TestProc")
class TestProc{

    @RequestHandler("*")
    public void newRequest(ProcRequest request){
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
    public void listFiles(@Param("directory")String dir, ProcRequest request){
    }

}
