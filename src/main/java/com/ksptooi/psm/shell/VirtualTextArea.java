package com.ksptooi.psm.shell;

import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.ConnectMode;
import org.apache.sshd.server.Environment;

public class VirtualTextArea {

    private final Environment env;
    private final AdvInputOutputCable cable;

    private final StringBuffer buf = new StringBuffer();
    private int vCursor = 0;

    private String header = "user>";


    public VirtualTextArea(AdvInputOutputCable cable, Environment env){
        this.env = env;
        this.cable = cable;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void render(){

        if(!cable.isConnect(ConnectMode.OUTPUT)){
            return;
        }

        /*
         * 1.将光标移到当前行的开头
         * 2.清除光标位置到行尾的内容
         * 3.清除当前行的全部内容
         */
        cable.print("\r").print("\033[K").print("\033[2K");

        //获取终端大小
        int columns = Integer.parseInt(env.getEnv().get("COLUMNS"));

        var content = buf.toString();

        var omitFactorVal = 11 + header.length();

        //渲染的内容超出终端宽度(省略多余字符)
        if(content.length() >= columns - omitFactorVal){
            int more = content.length() - (int)(columns - omitFactorVal);
            content = content.substring(0, (int)(columns - omitFactorVal));
            content = content + ">>more("+more+")";
        }

        cable.print(header);
        cable.print(content).print("\r");

        for(int i = 0; i < header.length(); i++){
            char c = header.charAt(i);
            if(isDoubleWidth(c)){
                cable.print("\033[C\033[C");  //中文字符在控制台上占两个位置
            }else {
                cable.print("\033[C"); // 其他字符占一个位置
            }
        }

        for (int i = 0; i < vCursor; i++) {
            //pw.write("\033[C"); //右移同步光标

            char c = buf.charAt(i);

            //双宽字符需要移动两次光标
            if(isDoubleWidth(c)){
                cable.print("\033[C\033[C");  //中文字符在控制台上占两个位置
            }else {
                cable.print("\033[C"); // 其他字符占一个位置
            }

        }

        cable.flush();

    }

    public void cursorUp(){

    }

    public void cursorDown(){

    }

    public void cursorLeft(){
        if(vCursor < 1){
            return;
        }
        vCursor--;
    }

    public void cursorRight(){
        if(vCursor >= buf.length()){
            return;
        }
        vCursor++;
    }

    public void insert(String str){

        //光标不是在末尾 处理插入
        if(vCursor != buf.length()){
            buf.insert(vCursor,str);
        }else {
            //光标在末尾 附加
            buf.append(str);
        }

        vCursor = vCursor + str.length();
    }

    public void backspace(){
        if(vCursor < 1){
            return;
        }
        buf.deleteCharAt(vCursor - 1);
        vCursor--;
    }

    public String getContent(){
        return buf.toString();
    }

    public void reset(){
        buf.setLength(0);
        vCursor = 0;
    }


    //判断字符是否是双宽字符
    private boolean isDoubleWidth(char c) {

        if ((int) c <= 127) {
            return false;
        } else {
            return true;
        }

    }

    public boolean isBlank(){
        if(vCursor < 1){
            return true;
        }
        if(buf.isEmpty() || buf.toString().trim().isEmpty()){
            return true;
        }
        return false;
    }


}
