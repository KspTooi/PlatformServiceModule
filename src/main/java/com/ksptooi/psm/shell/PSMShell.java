package com.ksptooi.psm.shell;

import com.ksptooi.Application;
import com.ksptooi.psm.processor.EventSchedule;
import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.processor.ServiceUnitManager;
import com.ksptooi.psm.processor.TaskManager;
import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.*;
import com.ksptooi.psm.processor.event.generic.ServiceUnitEvent;
import com.ksptooi.psm.utils.aio.*;
import com.ksptooi.psm.utils.aio.color.CyanDye;
import com.ksptooi.psm.vk.VK;
import jakarta.inject.Inject;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class PSMShell implements Command,Runnable{

    private static final Logger log = LoggerFactory.getLogger(PSMShell.class);
    private final String sessionId = UUID.randomUUID().toString();

    private ExitCallback exitCallback;
    private OutputStream eos;
    private OutputStream os;
    private PrintWriter pw;
    private InputStream is;
    private ChannelSession session;
    private Environment env;

    @Inject
    private ServiceUnitManager serviceUnitManager;

    @Inject
    private TaskManager taskManager;

    @Inject
    private EventSchedule eventSchedule;

    private Thread shellThread = null;

    //当前正在运行的前台任务
    private volatile Process currentTask = null;

    private boolean offline = false;

    private AdvancedInputOutputPort shellAioPort;
    private AdvInputOutputCable cable;
    private VirtualTextArea vt;

    @Override
    public void start(ChannelSession session, Environment env) throws IOException {

        this.session = session;
        this.env = env;

        final var v = Application.version;
        final var p = Application.platform;

        //创建Port和默认的Cable
        shellAioPort = new AdvInputOutputPort(is,os,env);
        cable = shellAioPort.createCable();
        cable.connect();
        cable.dye(CyanDye.pickUp)
                .w("Welcome To PlatformServiceModule(PSM/")
                .w(v)
                .w(" "+p)
                .w(")")
                .nextLine()
                .flush();

        vt = new VirtualTextArea(cable,env);
        vt.setHeader(getServerSession().getUsername());
        vt.render();

        //启动处理线程
        this.shellThread = new Thread(this);
        this.shellThread.start();
        log.info("账户:{} 已建立新会话:{}",session.getSession().getUsername(),sessionId);

        var e = new UserSessionLoggedEvent(this,getUserName(),getSessionId());
        eventSchedule.forward(e);
    }

    @Override
    public void destroy(ChannelSession session) throws Exception {
        offline = true;

        removeForeground();
        shellAioPort = null;
        cable.destroy();
        vt = null;

        if(shellThread!=null){
            shellThread.interrupt();
        }

        log.info("账户:{} 会话已离线:{}",session.getSession().getUsername(),sessionId);

        var e = new UserSessionClosedEvent(getUserName(),getSessionId());
        eventSchedule.forward(e);
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

            while (true){

                cable.read();

                final char[] rc = cable.getReadChars();
                final int rl = cable.getReadLen();

                //Shell原始输入事件
                if(triggerEvent(new ShellInputEvent(this,cable.getReadChars(),cable.getReadLen())).isCanceled()){
                    continue;
                };

                if(cable.match(VK.CTRL_C)){
                    if(currentTask == null || currentTask.getStage() != Process.STAGE_RUNNING){
                        continue;
                    }
                    taskManager.kill(currentTask.getPid());
                    continue;
                }

                //cable.printDebugText();

                //输入字符/或特殊符号
                if(cable.match(VK.USER_INPUT)){

                    //不允许键入CRLF
                    if(cable.containsCrlf()){
                        pw.print("输入错误.");
                        pw.flush();
                        continue;
                    }

                    ServiceUnitEvent forward = eventSchedule.forward(new UserTypingEvent(this,rc,rl, cable.getReadString()));

                    if(forward.isCanceled()){
                        continue;
                    }

                    if(currentTask != null){
                        continue;
                    }

                    var oldContent = vt.getContent();
                    vt.insert(cable.getReadString());
                    vt.render();

                    eventSchedule.forward(new AfterVirtualTextAreaChangeEvent(this,vt,vt.getContent(),oldContent));
                    continue;
                }

                if(currentTask != null){
                    continue;
                }

                //处理光标左右移动
                if(cable.match(VK.LEFT)){
                    vt.cursorLeft();
                    vt.render();
                    continue;
                }

                if(cable.match(VK.RIGHT)){
                    vt.cursorRight();
                    vt.render();
                    continue;
                }

                if(cable.match(VK.BACKSPACE)){
                    var oldContent = vt.getContent();
                    vt.backspace();
                    vt.render();
                    eventSchedule.forward(new AfterVirtualTextAreaChangeEvent(this,vt,vt.getContent(),oldContent));
                    continue;
                }

                //回车
                if(cable.match(VK.ENTER)){

                    if(vt.isBlank()){
                       continue;
                    }

                    StatementCommitEvent commitEvent = new StatementCommitEvent(this,vt.getContent());
                    eventSchedule.forward(commitEvent);

                    if(commitEvent.isCanceled()){
                        //重新渲染当前行并同步光标位置
                        vt.render();
                        continue;
                    }

                    String statement = vt.getContent();
                    vt.reset();

                    cable.nextLine().flush();

                    //statement组装为请求
                    ShellRequest req = new ShellRequest();
                    req.setStatement(statement);
                    req.setPattern(null);
                    req.setSeqArgument(new ArrayList<>());
                    req.setArgumentMap(new HashMap<>());
                    req.setShell(this);
                    req.setCable(shellAioPort.createCable());

                    Process forward = serviceUnitManager.forward(req);

                    if(forward == null){
                        vt.render();
                    }

                    continue;
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private ServiceUnitEvent triggerEvent(ServiceUnitEvent e){
        return eventSchedule.forward(e);
    }

    /**
     * 进程切换到Shell前台
     */
    public synchronized void setForeground(Process procTask){

        //当前有前台任务 并且前台任务正在运行
        if(currentTask != null && currentTask.getStage() != Process.STAGE_FINISHED){
            return;
        }

        //要切换的进程不能是非活跃的
        if(procTask.getStage() != Process.STAGE_RUNNING){
            return;
        }

        //切换进程到前台
        currentTask = procTask;
        var request = currentTask.getRequest();
        var cab = request.getCable();

        //进程Cab连接到Port
        cab.bindPort(shellAioPort);
        cab.connect(ConnectMode.OUTPUT);

        cab.addStateListener(sessionId,(c)->{
            var p = c.getPort();

            //cable已被迁移走
            if(!p.getPortId().equals(shellAioPort.getPortId())){
                removeForeground();
                return;
            }

            //cab已断开INPUT
            if(c.isConnect(ConnectMode.INPUT)){
                cable.connect(ConnectMode.INPUT);
            }

            //cab已断开Output
            removeForeground();
        });
    }

    /**
     * 将当前的前台进程切换为后台进程
     */
    public synchronized void removeForeground(){

        //当前没有前台进程
        if(currentTask == null) {
            return;
        }

        var cab = currentTask.getRequest().getCable();
        cab.removeStateListener(sessionId);
        cab.disconnect();
        currentTask = null;
        cable.connect();
        cable.nextLine().flush();
        vt.render();
    }

    public synchronized Process getCurrentProcess(){
        return currentTask;
    }
    
    public synchronized boolean hasForegroundProcess(){
        if(currentTask == null){
            return false;
        }
        if(currentTask.getStage() == Process.STAGE_RUNNING || currentTask.getInstance().isAlive()){
            return true;
        }
        return false;
    }

    public Environment getEnv(){
        return env;
    }

    public ChannelSession getSession(){
        return session;
    }
    public ServerSession getServerSession(){
        return session.getSession();
    }
    public boolean isOffline(){
        return this.offline;
    }

    public AdvancedInputOutputPort getAioPort(){
        return shellAioPort;
    }

    public AdvInputOutputCable getCable(){
        return cable;
    }

    public String getSessionId(){
        return this.sessionId;
    }

    public String getUserName(){
        return this.session.getSession().getUsername();
    }

    public VirtualTextArea getVirtualTextArea(){
        return vt;
    }
}
