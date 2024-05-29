package com.ksptooi.psm.processor.entity;

import com.ksptooi.psm.shell.ShellInstance;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
public class ProcTask {

    public ProcTask(ShellInstance u, Method m, ActiveProcessor p, HookTaskFinished finishHook, HookTaskToggle taskToggle){
        this.shell = u;
        this.method = m;
        this.processor = p;
        this.finishHook = finishHook;
        this.taskToggle = taskToggle;
    }

    @Setter
    private ShellInstance shell;
    private final Method method;
    private final ActiveProcessor processor;
    private Object[] params;
    @Getter(AccessLevel.NONE)
    private boolean isSetParams = false;
    private final HookTaskFinished finishHook;
    private final HookTaskToggle taskToggle;

    @Setter
    private int pid;
    @Setter
    private Thread instance;
    @Setter
    private int stage = 0; //preparing execute finished

    public static final Integer STAGE_PREPARING = 0;

    public static final Integer STAGE_RUNNING = 1;

    public static final Integer STAGE_FINISHED = 2;


    public void setParams(Object[] p){
        if(!isSetParams){
            params = p;
            isSetParams = true;
        }
    }

    public void toggleBackground(){
        taskToggle.toggle(true,this);
    }

    public void toggleForeground(){
        taskToggle.toggle(false,this);
    }


}
