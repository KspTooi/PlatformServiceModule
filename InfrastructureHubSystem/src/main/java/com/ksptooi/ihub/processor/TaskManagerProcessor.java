package com.ksptooi.ihub.processor;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.RequestHandler;
import com.ksptooi.psm.processor.RequestProcessor;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.Process;
import jakarta.inject.Inject;

import java.util.Map;

@RequestProcessor("bundled:proc:task_manager")
public class TaskManagerProcessor {

    @Inject
    private TaskManager taskManager;

    @RequestHandler("show task")
    public void taskList(ProcRequest request){

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
