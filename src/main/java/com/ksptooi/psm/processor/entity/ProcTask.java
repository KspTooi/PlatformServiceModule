package com.ksptooi.psm.processor.entity;

import com.ksptooi.psm.shell.ShellUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
public class ProcTask {

    public ProcTask(ShellUser u, Method m, ActiveProcessor p, HookTaskFinished finishHook){
        this.user = u;
        this.method = m;
        this.processor = p;
        this.finishHook = finishHook;
    }

    private final ShellUser user;
    private final Method method;
    private final ActiveProcessor processor;
    private Object[] params;
    @Getter(AccessLevel.NONE)
    private boolean isSetParams = false;
    private final HookTaskFinished finishHook;

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

}
