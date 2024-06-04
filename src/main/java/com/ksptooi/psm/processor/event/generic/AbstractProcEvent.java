package com.ksptooi.psm.processor.event.generic;

public abstract class AbstractProcEvent implements ProcEvent{

    /**
     * 当事件被取消 发布者不会继续执行其他动作
     */
    private boolean isCanceled = false;

    /**
     * 当事件被拦截后 不会再继续传递给下一个事件处理器执行
     */
    private boolean isIntercepted = false;


    public void cancel(){
        isCanceled = true;
    }
    public void intercept(){
        isIntercepted = true;
    }
    public boolean isCanceled(){
        return isCanceled;
    }
    public boolean isIntercepted(){
        return isIntercepted;
    }
}
