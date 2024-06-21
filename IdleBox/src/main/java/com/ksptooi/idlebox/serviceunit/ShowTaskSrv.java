package com.ksptooi.idlebox.serviceunit;

import com.ksptooi.psm.processor.OnActivated;
import com.ksptooi.psm.processor.ServiceUnit;

@ServiceUnit("IdleBox::ShowTask")
public class ShowTaskSrv {


    @OnActivated
    public void onload(){
        System.out.println("加载成功");
    }


}