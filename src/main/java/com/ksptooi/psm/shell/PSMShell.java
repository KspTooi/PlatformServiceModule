package com.ksptooi.psm.shell;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.ProcessorManager;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.HookTaskFinished;
import com.ksptooi.psm.processor.entity.HookTaskToggle;
import com.ksptooi.psm.processor.entity.ProcTask;
import com.ksptooi.psm.processor.event.CancellableEvent;
import com.ksptooi.psm.processor.event.ShellInputEvent;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.vk.ShellVK;
import com.ksptooi.psm.vk.VK;
import jakarta.inject.Inject;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class PSMShell implements Command,Runnable{

    private ExitCallback exitCallback;
    private OutputStream eos;
    private OutputStream os;
    private PrintWriter pw;
    private InputStream is;
    private ChannelSession session;
    private Environment env;
    private ShellInstance shell;

    private final StringBuffer vTextarea = new StringBuffer();
    private int vCursor;

    @Inject
    private ProcessorManager processorManager;

    @Inject
    private TaskManager taskManager;

    //当前正在运行的前台任务

    @Override
    public void start(ChannelSession session, Environment env) throws IOException {

        this.session = session;
        this.env = env;

        //启动处理线程
        Thread.ofVirtual().start(this);

        PrintWriter pw = new PrintWriter(os);
        pw.println("Hello PSMShell Welcome " + session.getSession().getUsername());
        pw.flush();

        shell = new ShellInstance(exitCallback,eos,os,pw,is,session,env);
    }

    @Override
    public void destroy(ChannelSession session) throws Exception {

    }

    @Override
    public void setExitCallback(ExitCallback exitCallback) {
        this.exitCallback = exitCallback;
    }

    @Override
    public void setErrorStream(OutputStream os) {
        this.eos = os;
    }

    @Override
    public void setInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.os = os;
        this.pw = new PrintWriter(os);
    }


    @Override
    public void run() {

        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ShellVK svk = new ShellVK(os,env);

            char[] read = new char[2400];

            while (true){

                int len = br.read(read);

                if(len < 1){
                    break;
                }

                //Shell原始输入事件
                ShellInputEvent event = new ShellInputEvent(shell,read,len);
                wrapEvent(event);
                processorManager.forward(event);
                if(event.isCanceled()){
                    break;
                }

                VK.print(read,len);

                //CTRL+C
                if(len == 1 && read[0] == VK.CTRL_C){
                    //终止命令执行线程

                    if(stickyTask == null || stickyTask.getStage() != ProcTask.STAGE_RUNNING){
                        continue;
                    }

                    taskManager.kill(stickyTask.getPid());
                    continue;
                }

                if(stickyTask != null){
                    continue;
                }

                //输入字符/或特殊符号
                if(VK.IS_INPUT(read,len)){

                    //不允许键入CRLF
                    if(VK.CONTAINS_CRLF(read,len)){
                        pw.print("输入错误.");
                        pw.flush();
                        continue;
                    }

                    //光标不是在末尾 处理插入
                    if(vCursor != vTextarea.length()){
                        vTextarea.insert(vCursor,read,0,len);
                    }else {
                        //光标在末尾 附加
                        vTextarea.append(read,0,len);
                    }

                    //vCursor++;
                    vCursor = vCursor + len;

                    //重新渲染当前行并同步光标位置
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                //处理光标左右移动
                if(VK.IS_LEFT(read,len)){
                    if(vCursor < 1){
                        continue;
                    }
                    vCursor--;

                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                if(VK.IS_RIGHT(read,len)){
                    if(vCursor >= vTextarea.length()){
                        continue;
                    }
                    vCursor++;
                    //svk.cursorRight();

                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                //退格 处理字符删除
                if(len == 1 && read[0] == VK.BACKSPACE){

                    if(vCursor < 1){
                        continue;
                    }

                    vTextarea.deleteCharAt(vCursor - 1);
                    vCursor--;

                    //重新渲染当前行并同步光标位置
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                //控制 UP (上一个命令)
                if(VK.IS_UP(read,len)){
                    System.out.println("UP");
                    continue;
                }
                if(VK.IS_DOWN(read,len)){
                    System.out.println("DOWN");
                    continue;
                }

                if(VK.IS_DELETE(read,len)){
                    System.out.println("DELETE");
                    continue;
                }

                //回车
                if(len == 1 && read[0] == VK.ENTER){

                    if(vTextarea.isEmpty() || vTextarea.toString().trim().isEmpty()){
                       continue;
                    }

                    StatementCommitEvent commitEvent = new StatementCommitEvent(shell,vTextarea.toString());
                    wrapEvent(commitEvent);
                    processorManager.forward(commitEvent);

                    if(commitEvent.isCanceled()){
                        //重新渲染当前行并同步光标位置
                        svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                        continue;
                    }

                    String statement = vTextarea.toString();
                    vTextarea.setLength(0);
                    vCursor = 0;

                    //svk.replaceCurrentLine("executed:: " + statement,0);
                    svk.nextLine();

                    //statement组装为请求
                    ProcRequest req = new ProcRequest();
                    req.setStatement(statement);
                    req.setPattern(null);
                    req.setParams(new ArrayList<>());
                    req.setParameters(new HashMap<>());
                    req.setShellInstance(shell);
                    req.setShellVk(svk);

                    HookTaskFinished hook = ()->{
                        svk.nextLine();
                        stickyTask = null;
                        System.out.println("exit hook");
                    };

                    HookTaskToggle hookToggle = (background,task)->{

                        if(background){
                            task.setShell(null);
                        }

                    };

                    ProcTask forward = processorManager.forward(req, hook,hookToggle);

                    //置顶任务到前台
                    triggerStickyTask(forward);

                    if(forward == null){
                        svk.nextLine();
                    }

                    continue;
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void wrapEvent(CancellableEvent e){
        e.setEos(eos);
        e.setOs(os);
        e.setPw(pw);
        e.setIs(is);
        e.setSession(session);
        e.setEnv(env);
    }

    private ProcTask stickyTask = null;

    /**
     * 触发置顶任务
     */
    public void triggerStickyTask(ProcTask procTask){
        if(stickyTask == null){
            stickyTask = procTask;
        }
    }

}
