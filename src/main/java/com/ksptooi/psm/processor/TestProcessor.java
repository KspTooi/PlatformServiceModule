package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.ProcTask;
import com.ksptooi.psm.processor.event.BadRequestEvent;
import com.ksptooi.psm.processor.hook.EventHandler;
import com.ksptooi.psm.shell.Colors;

import java.io.PrintWriter;

@RequestProcessor("TestProcessor")
public class TestProcessor {


    @EventHandler
    public void badRequestNotify(BadRequestEvent event){

        ProcRequest request = event.getRequest();
        PrintWriter w = request.getShellInstance().getPw();

        if(event.getErrorCode().equals(BadRequestEvent.ERR_INVOKE_EXCEPTION)){
            return;
        }

        if(event.getErrorCode().equals(BadRequestEvent.ERR_HANDLER_TYPE_INCONSISTENT)){
            w.print("fatal: ");
            w.print(request.getPattern());
            w.print(" ");
            w.print("无法处理请求,处理器与标定的不一致.");
            w.flush();
            return;
        }
        w.print(Colors.RED);
        w.print(request.getPattern());
        w.print(": 无法处理请求,匹配处理器失败.");
        w.print(Colors.RESET);
        w.flush();
    }

    @RequestHandler("test")
    public void test(ProcRequest req , ProcTask task) throws InterruptedException {

        PrintWriter p = task.getShell().getPw();
        p.print("这是一个测试命令 任务PID:"+task.getPid());
        p.flush();

        for (int i = 0; i < 5000; i++) {
            Thread.sleep(500);
            p.println(i);
            p.flush();
        }

    }

    @RequestHandler("*")
    public void newRequest(ProcRequest req) {

    }




}
