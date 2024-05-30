package com.ksptooi.psm.vk;

import com.ksptooi.psm.utils.FixedSizeStringBuffer;
import java.io.*;

public class TaskAdvInputOutputScreen {

    private FixedSizeStringBuffer fsBuffer;
    private final InputStream is;
    private final OutputStream os;

    private final PrintWriter p;
    private final BufferedReader br;

    private volatile boolean isRedirected = false;

    public TaskAdvInputOutputScreen(InputStream is, OutputStream os){
        this.is = is;
        this.os = os;
        p = new PrintWriter(os);
        br = new BufferedReader(new InputStreamReader(is));
        fsBuffer = new FixedSizeStringBuffer(81920);
    }

    public TaskAdvInputOutputScreen print(String s){

        if(isRedirected){
            fsBuffer.append(s);
            return this;
        }

        p.print(s);
        return this;
    }

    public TaskAdvInputOutputScreen println(String s){
        if(isRedirected){
            fsBuffer.append(s);
            return this;
        }
        p.println(s);
        return this;
    }

    public TaskAdvInputOutputScreen flush(){
        if(isRedirected){
            return this;
        }
        p.flush();
        return this;
    }

    public TaskAdvInputOutputScreen ok(){
        flush();
        return this;
    }

    public void redirect(){
        isRedirected = true;
    }

    public void resume(){
        isRedirected = false;
    }

}
