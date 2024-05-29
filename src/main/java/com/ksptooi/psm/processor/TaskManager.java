package com.ksptooi.psm.processor;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.entity.ProcTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Unit
public class TaskManager {

    private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private int nextPid = 1;
    private final TreeSet<Integer> availablePids = new TreeSet<>();

    private final Map<Integer,ProcTask> tasks = new ConcurrentHashMap<>();

    public void commit(ProcTask task){

        final String tName = "task-"+task.getUser().getSession().getSession().getUsername()+"-"+task.getMethod().getName();

        task.setPid(takePid());
        task.setStage(ProcTask.STAGE_RUNNING);
        tasks.put(task.getPid(),task);

        log.info("用户提交任务:{} PID:{}",tName,task.getPid());

        Thread thread = Thread.ofPlatform().name(tName).start(() -> {
            try {
                task.getMethod().invoke(task.getProcessor().getProc(),task.getParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseTask(task);
            log.info("任务结束:{}", tName);
        });

        task.setInstance(thread);
    }

    public void kill(int pid){

        ProcTask t = tasks.get(pid);

        if(t == null || t.getStage() != ProcTask.STAGE_RUNNING || t.getInstance() == null){
            return;
        }

        releaseTask(t);
        Thread instance = t.getInstance();
        instance.interrupt();
    }

    /**
     * 释放任务资源
     */
    private void releaseTask(ProcTask t){

        if(!tasks.containsKey(t.getPid())){
            return;
        }

        tasks.remove(t.getPid());
        t.setStage(ProcTask.STAGE_FINISHED);
        releasePid(t.getPid());
        t.getFinishHook().finished();
    }

    private synchronized int takePid(){
        if (availablePids.isEmpty()) {
            return nextPid++;
        } else {
            return availablePids.pollFirst();
        }
    }

    private synchronized void releasePid(int pid){
        availablePids.add(pid);
    }

}
