package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.UserTypingEvent;
import com.ksptooi.psm.processor.hook.EventHandler;
import com.ksptooi.psm.processor.hook.OnActivated;
import com.ksptooi.uac.core.annatatiotion.Param;

@ServiceUnit("TestSrvUnit")
public class TemplateServiceUnit {

    @OnActivated
    public void onActivated(){

    }

    //定义请求处理器
    @RequestHandler("example")
    public void example(ProcRequest req , Process task){

    }

    //定义事件处理器
    @EventHandler(global = true)
    public void userType(UserTypingEvent event){
        
    }


}
