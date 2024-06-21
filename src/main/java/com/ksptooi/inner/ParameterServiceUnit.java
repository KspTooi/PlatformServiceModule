package com.ksptooi.inner;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.utils.aio.color.GreenDye;
import com.ksptooi.uac.core.annatatiotion.Param;

@ServiceUnit("bundled::test::ParameterServiceUnit")
public class ParameterServiceUnit {


    @OnActivated
    public void activated(){

    }

    @RequestHandler("echo")
    public void echo(@Param("fileName") String fileName, ShellRequest req ,@Param("size") String size){

        var cable = req.getCable().connect(ConnectMode.OUTPUT);
        cable.dye(GreenDye.pickUp);
        cable.print("输入参数为:"+fileName + ","+size);
        //cable.flush();

    }

}
