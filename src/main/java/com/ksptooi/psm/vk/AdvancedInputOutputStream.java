package com.ksptooi.psm.vk;

import org.apache.sshd.server.Environment;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程独占的IO
 */
public class AdvancedInputOutputStream {

    private final ReentrantLock lockI = new ReentrantLock();
    private final ReentrantLock lockO = new ReentrantLock();

    private final InputStream is;
    private final OutputStream os;
    private final Environment env;

    private final BufferedReader b;
    private final PrintWriter p;

    private final char[] rc = new char[8192]; //readBuffer
    private volatile int rl = 0; //readLen

    public AdvancedInputOutputStream(InputStream is, OutputStream os, Environment env){
        this.is = is;
        this.os = os;
        this.env = env;
        this.b = new BufferedReader(new InputStreamReader(is));
        this.p = new PrintWriter(os);
        attachInput();
        attachOutput();
    }

    public AdvancedInputOutputStream print(String a){
        checkHeldOutput();
        p.print(a);
        return this;
    }

    public AdvancedInputOutputStream println(String a){
        checkHeldOutput();
        p.print(a);
        return this;
    }

    public AdvancedInputOutputStream flush(){
        checkHeldOutput();
        p.flush();
        return this;
    }

    public void read() throws IOException {
        checkHeldInput();
        rl = b.read(rc);
        if(rl < 1){
            throw new IOException();
        }
    }

    public int directRead(char[] c) throws IOException {
        checkHeldInput();
        return b.read(c);
    }

    public int directRead() throws IOException{
        checkHeldInput();
        return b.read();
    }

    public void checkHeldInput(){
        if(!isHeldInput()){
            throw new IOBusyException();
        }
    }
    public void checkHeldOutput(){
        if(!isHeldOutput()){
            throw new IOBusyException();
        }
    }
    public boolean isHeldInput(){
        return lockI.isHeldByCurrentThread();
    }
    public boolean isHeldOutput(){
        return lockO.isHeldByCurrentThread();
    }
    public boolean attachInput(){
        return lockI.tryLock();
    }
    public boolean attachOutput(){
        return lockO.tryLock();
    }
    public void detachInput(){
        lockI.unlock();
    }
    public void detachOutput(){
        lockO.unlock();
    }



    public boolean match(int[] c){
        return mat(c);
    }

    public boolean match(int k){

        if(rl < 1){
            return false;
        }

        if(k == VK.HOME){
            return mat(27,91,72) || mat(27,91,49,126);
        }
        if(k == VK.END){
            return mat(27,91,70) || mat(27,91,52,126);
        }

        if(k == VK.USER_INPUT){
            return isUserTypes();
        }

        return false;
    }


    /**
     * 检查读取的数据是否匹配给定的序列
     */
    private boolean mat(int... seq){

        if(rl != seq.length){
            return false;
        }

        for(int i = 0 ; i < seq.length ; i++){
            if(rc[i] != seq[i]){
                return false;
            }
        }

        return true;
    }

    private boolean isUserTypes(){

        //在只有一个字符的情况下 第一个字符为CRLF 不能判定为输入 判定为回车
        if(rl == 1 && ( rc[0] == '\r' || rc[0] == '\n' )){
            return false;
        }

        for (int i = 0; i < rl; i++) {
            char ch = rc[i];
            if (!((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') ||
                    (ch >= '!' && ch <= '/') || (ch >= ':' && ch <= '@') ||
                    (ch >= '[' && ch <= '`') || (ch >= '{' && ch <= '~') ||
                    (ch >= '\u4e00' && ch <= '\u9fa5') ||   // 中文字符
                    (ch >= '\u3000' && ch <= '\u303F') ||   // 中文标点符号
                    (ch >= '\uFF01' && ch <= '\uFF5E') ||   // 全角字符
                    ch == ' ' || ch == '\r' || ch == '\n')) {
                //System.out.println("命中失败 字符:" + ch);
                return false;
            }
        }
        return true;
    }

    public boolean containsCrlf(){
        for (int i = 0; i < rl; i++) {
            if (rc[i] == '\r' || rc[i] == '\n') {
                return true;
            }
        }
        return false;
    }

    public int getReadLen(){
        return rl;
    }
    public char[] getReadChars(){
        return rc;
    }

}