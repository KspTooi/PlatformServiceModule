package com.ksptooi.inner;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.ShellInputEvent;
import com.ksptooi.psm.shell.PSMShell;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.utils.aio.color.GreenDye;
import com.ksptooi.psm.utils.aio.color.RedDye;
import com.ksptooi.psm.vk.VK;
import com.ksptooi.uac.core.annatatiotion.Param;
import jakarta.inject.Inject;
import java.util.Map;

@ServiceUnit("bundled:TaskManagerSrvUnit")
public class TaskManagerSrvUnit {

    @Inject
    private TaskManager taskManager;

    @EventHandler(global = true)
    public void onShellInput(ShellInputEvent event){

        if(!event.match(VK.CTRL_D)){
            return;
        }

        var shell = event.getUserShell();

        //按下CTRL_D将当前进程置于后台
        if(event.match(VK.CTRL_D)){
            if(shell.hasForegroundProcess()){
                shell.removeForeground();
            }
        }

    }

    @Background
    @RequestHandler("attach")
    public void attachProcess(ShellRequest req, @Param("pid")String pid){

        var cable = req.getCable()
                .connect(ConnectMode.OUTPUT)
                .dye(RedDye.pickUp);

        var shell = req.getShell();
        var process = taskManager.getProcess(Integer.parseInt(pid));

        if(process == null){
            cable.println("进程不存在").flush();
            return;
        }

        cable.dye(GreenDye.pickUp);
        cable.w("已切换到进程:")
                .w(process.getPid())
                .w("(")
                .w(process.getTaskName())
                .w(")")
                .nextLine()
                .flush();

        shell.removeForeground();
        shell.setForeground(process);
    }

    @RequestHandler("show task")
    public void taskList(ShellRequest request){

        var aio = request.getCable();

        Map<Integer, Process> tasks = taskManager.getTasks();

        aio.nextLine();
        aio.print("当前正在运行的进程:").print(tasks.size());
        aio.nextLine();

        for(var item : tasks.entrySet()){

            var t = item.getValue();

            aio.print("PID:").print(item.getKey())
                    .print("\t进程名:").print(t.getTaskName())
                    .print("\t状态:").print(t.getStageStr())
                    .print("\t用户:").print(t.getRequest().getShell().getServerSession().getUsername());

            aio.nextLine();
        }

        aio.flush();
    }

}
