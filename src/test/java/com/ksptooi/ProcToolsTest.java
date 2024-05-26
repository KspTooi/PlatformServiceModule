package com.ksptooi;

import com.ksptooi.psm.processor.ProcTools;
import com.ksptooi.psm.processor.TestProcessor;
import com.ksptooi.psm.processor.entity.RequestDefine;

import java.util.List;

public class ProcToolsTest {

    public static void main(String[] args) {

        TestProcessor tp = new TestProcessor();

        List<RequestDefine> requestDefine = ProcTools.getRequestDefine(TestProcessor.class);

        System.out.println(requestDefine);

    }

}
