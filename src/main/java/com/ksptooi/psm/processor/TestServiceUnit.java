package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.UserTypingEvent;
import com.ksptooi.psm.shell.Colors;

@ServiceUnit("TestSrvUnit")
public class TestServiceUnit {

    @RequestHandler("test")
    public void test(ShellRequest req , Process task) throws InterruptedException {

        var p = req.getCable();
        p.print(Colors.BLUE);
        p.print("这是一个测试进程 它会在2000秒后自动结束 使用CTRL+C强制结束 进程PID:"+task.getPid()).print(Colors.RESET);
        p.nextLine().flush();

        for (int i = 0; i < 5000; i++) {
            Thread.sleep(500);
            p.println(i);
            p.flush();
        }

    }

    @EventHandler(global = true)
    public void userType(UserTypingEvent event){

        //当前有前台任务正在运行
        if(event.getUserShell().hasForegroundProcess()){
            return;
        }

        var cable = event.getUserShell().getCable();

        if(event.getContent().equals("G")){
            cable.print("全局监听用户按下G键");
            cable.nextLine();
            cable.flush();
        }

    }

    @EventHandler
    public void userType(UserTypingEvent event, ShellRequest request){
        var cable = request.getCable();
        cable.print("用户在前台进程内按键:");
        cable.print(event.getContent());
        cable.nextLine();
        cable.flush();
    }


}
