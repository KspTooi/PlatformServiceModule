package com.ksptooi.inner;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.RequestHandler;
import com.ksptooi.psm.processor.RequestProcessor;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.RunningTask;
import jakarta.inject.Inject;

import java.util.Map;

@RequestProcessor("bundled:proc:task_manager")
public class TaskManagerProcessor {

    @Inject
    private TaskManager taskManager;

    @RequestHandler("show task")
    public void taskList(ProcRequest request){

        var aio = request.getAio();

        Map<Integer, RunningTask> tasks = taskManager.getTasks();

        aio.nextLine();
        aio.print("当前正在运行的进程:").print(tasks.size());
        aio.nextLine();

        for(var item : tasks.entrySet()){

            var t = item.getValue();

            aio.print("PID:").print(item.getKey())
                    .print("  进程名:").print(t.getName())
                    .print("  状态:").print(t.getStageStr())
                    .print("  用户:").print(t.getRequest().getShell().getServerSession().getUsername());

            aio.nextLine();
        }

        aio.flush();
    }

}