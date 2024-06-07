package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.RunningTask;
import com.ksptooi.psm.processor.event.BadRequestEvent;
import com.ksptooi.psm.processor.event.RequestForwardEvent;
import com.ksptooi.psm.processor.hook.EventHandler;
import com.ksptooi.psm.shell.Colors;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.AdvInputOutputStream;

@RequestProcessor("TestProcessor")
public class TestProcessor {

    @RequestHandler("test")
    public void test(ProcRequest req , RunningTask task) throws InterruptedException {

        var p = req.getCable();
        p.print("这是一个测试命令 任务PID:"+task.getPid());
        p.nextLine();

        for (int i = 0; i < 5000; i++) {
            Thread.sleep(500);
            p.println(i);
            p.flush();
        }

    }

    @RequestHandler("*")
    public void newRequest(ProcRequest req) {
    }


    @EventHandler(global = true)
    public void event(RequestForwardEvent e){

        e.cancel();
        AdvInputOutputCable aio = e.getRequest().getCable();

    }


}
