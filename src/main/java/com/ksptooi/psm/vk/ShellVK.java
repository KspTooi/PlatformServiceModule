package com.ksptooi.psm.vk;

import org.apache.sshd.server.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ShellVK {

    private OutputStream os;
    private PrintWriter pw;
    private StringBuffer sb = new StringBuffer();
    private Environment env;

    public ShellVK(OutputStream os, Environment env){
        this.os = os;
        this.pw = new PrintWriter(os);
        this.env = env;
    }

    public void press(char[] c,int len) throws IOException {
        sb.append(c,0,len);
        pw.write(sb.toString());
        pw.flush();
        sb.setLength(0);
    }


    public void replaceCurrentLine(String str){
        pw.write("\r");
        pw.write("\033[K");
        pw.write(str);
        pw.flush();
    }


    public void replaceCurrentLine(String str,int cursor){
        pw.write("\r");
        pw.write("\033[K");
        pw.write("\033[2K");

        //获取终端大小
        int columns = Integer.parseInt(env.getEnv().get("COLUMNS"));

        String print = str;

        //渲染的内容超出终端宽度(省略多余字符)
        if(print.length() >= columns - 11){
            int more = print.length() - (int)(columns - 11);
            print = print.substring(0, (int)(columns - 11));
            print = print + ">>more("+more+")";
        }

        pw.write(print);
        pw.write("\r");

        for (int i = 0; i < cursor; i++) {
            //pw.write("\033[C"); //右移同步光标

            char c = str.charAt(i);

            //双宽字符需要移动两次光标
            if(isDoubleWidth(c)){
                pw.write("\033[C\033[C");  //中文字符在控制台上占两个位置
            }else {
                pw.write("\033[C"); // 其他字符占一个位置
            }

        }

        pw.flush();
    }

    public void nextLine(){
        pw.write("\r\n");
        pw.flush();
    }

    public void backspace(){
        pw.print("\b \b"); // 使用 ANSI 控制序列删除当前位置字
        // 发送光标左移的控制序列
        //pw.print("\033[D");
        // 发送删除字符的控制序列
        //pw.print("\033[P");
        pw.flush();
    }

    public void deleteDoubleWidth(){
        pw.print("\b \b");
        pw.print("\b \b");
        pw.flush();
    }

    public void cursorLeft() {
        pw.print("\033[D"); // ANSI 控制序列，光标左移一格
        pw.flush();
    }

    public void cursorRight() {
        pw.print("\033[C"); // ANSI 控制序列，光标右移一格
        pw.flush();
    }

    public void synCursor(String text,int vCursor){
        pw.write("\r");

        for (int i = 0; i < vCursor; i++) {
            //pw.write("\033[C"); //右移同步光标

            char c = text.charAt(i);

            //双宽字符需要移动两次光标
            if(isDoubleWidth(c)){
                pw.write("\033[C\033[C");  //中文字符在控制台上占两个位置
            }else {
                pw.write("\033[C"); // 其他字符占一个位置
            }

        }

        pw.flush();
    }


    //判断字符是否是双宽字符
    public boolean isDoubleWidth(char c) {

        if ((int) c <= 127) {
            return false;
        } else {
            return true;
        }

/*        Pattern p = Pattern.compile("[^x00-xff]");
        Matcher m = p.matcher(c + "");
        while (m.find()){
            return true;
        }

        return false;*/

        // 常见的双宽字符范围
/*        return (c >= '\u1100' && c <= '\u115F') ||      // Hangul Jamo
                (c >= '\u2329' && c <= '\u232A') ||     // CJK部首补充
                (c >= '\u2E80' && c <= '\uA4CF') ||    // CJK部首
                (c >= '\uA840' && c <= '\uA877') ||    // Phags-pa
                (c >= '\uAC00' && c <= '\uD7A3') ||    // Hangul Syllables
                (c >= '\uF900' && c <= '\uFAFF') ||    // CJK兼容象形文字
                (c >= '\uFE10' && c <= '\uFE19') ||    // 表格形式
                (c >= '\uFE30' && c <= '\uFE6F') ||    // CJK兼容形式
                (c >= '\uFF00' && c <= '\uFF60') ||    // 全角ASCII、全角中英标点符号
                (c >= '\uFFE0' && c <= '\uFFE6') ||    // 全角数字、全角货币符号
                (c >= '\u20000' && c <= '\u2A6DF') ||  // CJK扩展B
                (c >= '\u2A700' && c <= '\u2B73F') ||  // CJK扩展C
                (c >= '\u2B740' && c <= '\u2B81F') ||  // CJK扩展D
                (c >= '\u2B820' && c <= '\u2CEAF') ||  // CJK扩展E
                (c >= '\u2CEB0' && c <= '\u2EBEF') ||  // CJK扩展F
                (c >= '\u2F800' && c <= '\u2FA1F');    // CJK兼容扩展*/
    }

}
