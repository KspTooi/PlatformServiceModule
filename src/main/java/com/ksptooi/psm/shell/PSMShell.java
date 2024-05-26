package com.ksptooi.psm.shell;

import com.ksptooi.psm.processor.ProcRequest;
import com.ksptooi.psm.processor.ProcessorManager;
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

    private final StringBuffer vTextarea = new StringBuffer();
    private int vCursor;

    @Inject
    private ProcessorManager processorManager;

    @Override
    public void start(ChannelSession session, Environment env) throws IOException {

        this.session = session;
        this.env = env;

        //启动处理线程
        Thread.ofVirtual().start(this);

        PrintWriter pw = new PrintWriter(os);
        pw.println("Hello PSMShell Welcome " + session.getSession().getUsername());
        pw.flush();
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
            ShellVK svk = new ShellVK(os);

            char[] read = new char[2400];

            while (true){

                int len = br.read(read);

                VK.print(read,len);

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
                    //svk.cursorLeft();
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

                //CTRL+C
                if(len == 1 && read[0] == VK.CTRL_C){
                    //终止命令执行线程
                    pw.println("command abort");
                    pw.flush();
                    continue;
                }

                //回车
                if(len == 1 && read[0] == VK.ENTER){

                    String statement = vTextarea.toString();
                    vTextarea.setLength(0);
                    vCursor = 0;

                    //svk.replaceCurrentLine("executed:: " + statement,0);
                    //svk.nextLine();

                    //statement组装为请求
                    ProcRequest req = new ProcRequest();
                    req.setStatement(statement);
                    req.setName(null);
                    req.setParameter(new ArrayList<>());
                    req.setParameters(new HashMap<>());
                    req.setSession(session);
                    req.setIs(is);
                    req.setOs(os);
                    req.setEos(eos);
                    req.setPw(pw);
                    req.setShellVk(svk);
                    processorManager.forward(req);
                    continue;
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
