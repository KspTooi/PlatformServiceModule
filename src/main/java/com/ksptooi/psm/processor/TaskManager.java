package com.ksptooi.psm.processor;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.entity.RunningTask;
import org.apache.sshd.server.channel.ChannelSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Unit
public class TaskManager {

    private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private int nextPid = 1;
    private final TreeSet<Integer> availablePids = new TreeSet<>();

    private final Map<Integer, RunningTask> tasks = new ConcurrentHashMap<>();

    private RunningTask curTask;

    public void commit(RunningTask task){

        ChannelSession session = task.getRequest().getShellInstance().getSession();

        final String tName = "task-"+session.getSession().getUsername()+"-"+task.getTarget().getName();

        task.setPid(takePid());
        task.setStage(RunningTask.STAGE_RUNNING);
        tasks.put(task.getPid(),task);

        log.info("用户提交任务:{} PID:{}",tName,task.getPid());

        Thread thread = Thread.ofPlatform().name(tName).start(() -> {

            //创建子AIO 并置顶
            

            try {
                task.getTarget().invoke(task.getProcessor().getProc(),task.getInjectParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseTask(task);
            log.info("任务结束:{}", tName);
        });

        task.setInstance(thread);
    }

    public void kill(int pid){

        RunningTask t = tasks.get(pid);

        if(t == null || t.getStage() != RunningTask.STAGE_RUNNING || t.getInstance() == null){
            return;
        }

        releaseTask(t);
        Thread instance = t.getInstance();
        instance.interrupt();
    }

    /**
     * 释放任务资源
     */
    private void releaseTask(RunningTask t){

        if(!tasks.containsKey(t.getPid())){
            return;
        }

        tasks.remove(t.getPid());
        t.setStage(RunningTask.STAGE_FINISHED);
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

    /**
     * 将正在运行的任务切换到前台
     */
    public void toggleCurTask(RunningTask task){
        curTask = task;
        task.getRequest().getAio().attachOutput();
    }

    /**
     * 任务切换为后台运行
     */
    public void toggleCurTask(){
        if(curTask!=null){
            curTask.getRequest().getAio().detachOutput();
            curTask.getRequest().getAio().detachInput();
            curTask = null;
        }
    }

    /**
     * 获取当前正在前台运行的任务
     */
    public RunningTask getCurTask(){
        return curTask;
    }

}
