package com.ksptooi.psm.processor;

import com.ksptooi.psm.shell.ShellUser;
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

    //请求Pattern
    private String pattern;

    //请求参数
    private List<String> params;

    //请求参数组
    private Map<String,String> parameters;

    private ShellUser user;

    private ShellVK shellVk;

}
