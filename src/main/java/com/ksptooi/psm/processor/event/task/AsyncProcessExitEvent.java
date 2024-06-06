package com.ksptooi.psm.processor.event.task;

import com.ksptooi.psm.processor.entity.RunningTask;
import com.ksptooi.psm.processor.event.generic.AbstractProcEvent;
import com.ksptooi.psm.shell.PSMShell;
import lombok.Getter;

/**
 * 进程退出事件(这个事件不能被取消 也无法阻止进程创建)
 */
@Getter
public class AsyncProcessExitEvent extends AbstractProcEvent {

    private final RunningTask process;

    public AsyncProcessExitEvent(RunningTask process) {
        this.process = process;
    }

    @Override
    public PSMShell getUserShell() {
        return process.getRequest().getShell();
    }

}
