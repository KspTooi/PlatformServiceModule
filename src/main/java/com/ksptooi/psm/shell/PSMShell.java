package com.ksptooi.psm.shell;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.ProcessorManager;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.HookTaskFinished;
import com.ksptooi.psm.processor.entity.HookTaskToggle;
import com.ksptooi.psm.processor.entity.RunningTask;
import com.ksptooi.psm.processor.event.CancellableEvent;
import com.ksptooi.psm.processor.event.ProcEvent;
import com.ksptooi.psm.processor.event.ShellInputEvent;
import com.ksptooi.psm.processor.event.StatementCommitEvent;
import com.ksptooi.psm.vk.AdvInputOutputStream;
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

    private Thread shellThread = null;

    //当前正在运行的前台任务
    private RunningTask currentTask = null;

    @Override
    public void start(ChannelSession session, Environment env) throws IOException {

        this.session = session;
        this.env = env;

        //启动处理线程
        this.shellThread = Thread.ofVirtual().start(this);

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

            AdvInputOutputStream aios = new AdvInputOutputStream(is,os,env);

            //BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ShellVK svk = new ShellVK(os,env);

            //char[] read = new char[2400];

            while (true){

                aios.read();

                final char[] rc = aios.getReadChars();
                final int rl = aios.getReadLen();

                //Shell原始输入事件
                if(((CancellableEvent)triggerEvent(new ShellInputEvent(shell,aios.getReadChars(),aios.getReadLen()))).isCanceled()){
                    continue;
                };

                if(aios.match(VK.CTRL_C)){
                    if(currentTask == null || currentTask.getStage() != RunningTask.STAGE_RUNNING){
                        continue;
                    }
                    taskManager.kill(currentTask.getPid());
                    continue;
                }

                aios.printDebugText();

                if(currentTask != null){
                    continue;
                }

                //输入字符/或特殊符号
                if(aios.match(VK.USER_INPUT)){

                    //不允许键入CRLF
                    if(aios.containsCrlf()){
                        pw.print("输入错误.");
                        pw.flush();
                        continue;
                    }

                    //光标不是在末尾 处理插入
                    if(vCursor != vTextarea.length()){
                        vTextarea.insert(vCursor,rc,0,rl);
                    }else {
                        //光标在末尾 附加
                        vTextarea.append(rc,0,rl);
                    }

                    //vCursor++;
                    vCursor = vCursor + rl;

                    //重新渲染当前行并同步光标位置
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                //处理光标左右移动
                if(aios.match(VK.LEFT)){
                    if(vCursor < 1){
                        continue;
                    }
                    vCursor--;
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                if(aios.match(VK.RIGHT)){
                    if(vCursor >= vTextarea.length()){
                        continue;
                    }
                    vCursor++;
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                if(aios.match(VK.BACKSPACE)){
                    if(vCursor < 1){
                        continue;
                    }
                    vTextarea.deleteCharAt(vCursor - 1);
                    vCursor--;
                    //重新渲染当前行并同步光标位置
                    svk.replaceCurrentLine(vTextarea.toString(),vCursor);
                    continue;
                }

                //回车
                if(aios.match(VK.ENTER)){

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
                    //req.setShellVk(svk);
                    req.setAio(aios);

                    HookTaskFinished hook = ()->{
                        svk.nextLine();
                        currentTask = null;
                        System.out.println("exit hook");
                    };

                    HookTaskToggle hookToggle = (background,task)->{

                    };

                    RunningTask forward = processorManager.forward(req, hook,hookToggle);

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


    private ProcEvent triggerEvent(CancellableEvent e){
        wrapEvent(e);
        return processorManager.forward(e);
    }

    private void wrapEvent(CancellableEvent e){
        e.setEos(eos);
        e.setOs(os);
        e.setPw(pw);
        e.setIs(is);
        e.setSession(session);
        e.setEnv(env);
    }

    /**
     * 触发置顶任务
     */
    public void triggerStickyTask(RunningTask procTask){
        if(currentTask == null){
            currentTask = procTask;
        }
    }

}
