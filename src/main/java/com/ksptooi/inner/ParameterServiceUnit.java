package com.ksptooi.inner;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.utils.aio.color.GreenDye;
import com.ksptooi.uac.core.annatatiotion.Param;

import java.util.List;

@ServiceUnit("bundled::test::ParameterServiceUnit")
public class ParameterServiceUnit {


    @OnActivated
    public void activated(){

    }


    @RequestHandler("echo1")
    public void echo1(ShellRequest req, @Param("names")List<String> names){



    }


    @RequestHandler("echo")
    public void echo(ShellRequest request,@Param("fileName") String fileName,@Param("size") Double size){

        var cable = request.getCable().connect(ConnectMode.OUTPUT).dye(GreenDye.pickUp);
        cable.w("输入参数 ").w("FileName:").w(fileName);
        cable.w(" Size:").w(size);

    }

}
