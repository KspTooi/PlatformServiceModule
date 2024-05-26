package com.ksptooi.psm.processor;

import com.ksptooi.psm.vk.ShellVK;
import lombok.Data;
import org.apache.sshd.server.channel.ChannelSession;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Data
public class ProcRequest {

    //请求原始语句
    private String statement;

    //请求名称
    private String name;

    //请求参数
    private List<String> parameter;

    //请求参数组
    private Map<String,String> parameters;

    private ChannelSession session;

    private InputStream is;

    private OutputStream os;

    private PrintWriter pw;

    private OutputStream eos;

    private ShellVK shellVk;

}
