package com.ksptooi.psm.shell;

import com.ksptooi.psm.utils.VK;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;


public class PSMShell implements Command,Runnable{

    private ExitCallback exitCallback;
    private OutputStream eos;
    private OutputStream os;
    private InputStream is;

    private ChannelSession session;
    private Environment env;

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
    }



    @Override
    public void run() {

        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(os);

            StringBuilder sb = new StringBuilder();

            char[] read = new char[24];

            while (true){

                int len = br.read(read);

                VK.print(read,len);

                //控制 UP (上一个命令)
                if(VK.IS_UP(read,len)){
                    System.out.println("UP");
                    continue;
                }
                if(VK.IS_DOWN(read,len)){
                    System.out.println("DOWN");
                    continue;
                }

                //CTRL C
                if(len == 1 && read[0] == VK.CTRL_C){
                    //终止命令执行线程
                    pw.println("command abort");
                }

                //回显输入字符
                sb.append(read,0,len);
                pw.print(sb.toString());
                pw.flush();
                sb.setLength(0);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
