package com.ksptooi.inner;

import com.ksptooi.psm.processor.RequestHandler;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.Process;
import jakarta.inject.Inject;

import java.util.Map;

@ServiceUnit("bundled:TaskManagerSrvUnit")
public class TaskManagerSrvUnit {

    @Inject
    private TaskManager taskManager;

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
