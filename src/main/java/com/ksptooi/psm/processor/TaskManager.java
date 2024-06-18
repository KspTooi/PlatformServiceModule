package com.ksptooi.psm.processor;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.task.AsyncProcessCommitEvent;
import com.ksptooi.psm.processor.event.task.AsyncProcessExitEvent;
import jakarta.inject.Inject;
import lombok.Getter;
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

    @Getter
    private final Map<Integer, Process> tasks = new ConcurrentHashMap<>();

    @Inject
    private EventSchedule eventSchedule;

    public void commit(Process task){

        final var request = task.getRequest();
        final ChannelSession session = request.getShell().getSession();
        final var taskName = task.getTaskName();
        final var username = session.getSession().getUsername();
        final var pid = takePid();

        task.setPid(pid);
        task.setStage(Process.STAGE_RUNNING);
        tasks.put(task.getPid(),task);

        log.info("用户:{} 启动进程:{} PID:{}", username,taskName,task.getPid());

        Thread thread = Thread.ofVirtual().name(taskName).start(() -> {

            //提交进程创建事件
            var event = new AsyncProcessCommitEvent(task);
            eventSchedule.forward(event);

            //进程切换到前台
            if(!task.isBackground()){
                request.getShell().setForeground(task);
            }

            try {
                task.getTarget().invoke(task.getServiceUnit().getSrvUnit(),task.getInjectParams());
            } catch (Exception e){
                if(e.getCause() instanceof InterruptedException){
                    log.info("进程被中止:{}", taskName);
                }else {
                    e.printStackTrace();
                }
            }

            releaseTask(task);

            //提交进程退出事件
            var exit = new AsyncProcessExitEvent(task);
            eventSchedule.forward(exit);

            //进程从前台移除
            if(!task.isBackground()){
                request.getShell().removeForeground();
            }

            //log.info("进程退出:{}", taskName);
        });

        task.setInstance(thread);
    }

    public void kill(int pid){

        Process t = tasks.get(pid);

        if(t == null || t.getStage() != Process.STAGE_RUNNING || t.getInstance() == null){
            return;
        }

        releaseTask(t);
        Thread instance = t.getInstance();
        instance.interrupt();
    }

    /**
     * 释放任务资源
     */
    private void releaseTask(Process t){

        if(!tasks.containsKey(t.getPid())){
            return;
        }

        tasks.remove(t.getPid());
        t.setStage(Process.STAGE_FINISHED);
        releasePid(t.getPid());

        //销毁AIO
        t.getRequest().getCable().destroy();
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

    public Process getProcess(int pid){
        return tasks.get(pid);
    }

}
