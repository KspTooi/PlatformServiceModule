package com.ksptooi.psm.processor.entity;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.TaskManager;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 用于表示一个正在运行中的Task
 */
public class RunningTask {



    @Getter
    private int pid = -1; //当前Task的PID

    @Getter
    private String taskName;

    //原始请求对象
    @Getter
    private ProcRequest request;

    //Task所在的处理器
    @Getter
    private ActiveProcessor processor;

    //任务的执行函数
    @Getter
    private Method target;

    //执行函数(Target)所需要注入的实参
    @Getter
    private Object[] injectParams;

    //Task的运行进程
    @Getter
    private Thread instance;

    //Task结束hook
    @Getter
    private HookTaskFinished finishHook;

    //表示Task的执行阶段 preparing execute finished
    @Setter @Getter
    private int stage = 0;

    //运行该任务的任务管理器
    @Getter
    private TaskManager taskManager;

    public void setPid(int pid){
        if(this.pid == -1){
            this.pid = pid;
        }
    }
    public void setRequest(ProcRequest request){
        if(this.request == null){
            this.request = request;
        }
    }
    public void setProcessor(ActiveProcessor proc){
        if(this.processor == null){
            this.processor = proc;
        }
    }
    public void setTarget(Method tgt){
        if(this.target == null){
            this.target = tgt;
        }
    }
    public void setInjectParams(Object[] params){
        if(this.injectParams == null){
            this.injectParams = params;
        }
    }
    public void setInstance(Thread thread){
        if(this.instance == null){
            this.instance = thread;
        }
    }
    public void setFinishHook(HookTaskFinished hook){
        if(this.finishHook == null){
            this.finishHook = hook;
        }
    }
    public void setTaskManager(TaskManager manager) {
        if(this.taskManager == null){
            this.taskManager = manager;
        }
    }
    public void setTaskName(String name) {
        if(this.taskName == null){
            this.taskName = name;
        }
    }

    public String getStageStr(){

        if(stage == 0){
            return "PREPARING";
        }
        if(stage == 1){
            return "RUNNING";
        }
        if(stage == 2){
            return "FINISHED";
        }

        return "UNKNOWN";
    }

    public static final Integer STAGE_PREPARING = 0;
    public static final Integer STAGE_RUNNING = 1;
    public static final Integer STAGE_FINISHED = 2;
}
