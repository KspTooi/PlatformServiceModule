package com.ksptooi.psm.vk;

/**
 * 缓存与VK匹配器
 */
public class BufferedAndMatcher {

    protected final char[] rb = new char[8192]; //readBuffer
    protected volatile int rl = 0; //readLen

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
            if(rb[i] != seq[i]){
                return false;
            }
        }

        return true;
    }

    private boolean isUserTypes(){

        //在只有一个字符的情况下 第一个字符为CRLF 不能判定为输入 判定为回车
        if(rl == 1 && ( rb[0] == '\r' || rb[0] == '\n' )){
            return false;
        }

        for (int i = 0; i < rl; i++) {
            char ch = rb[i];
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
            if (rb[i] == '\r' || rb[i] == '\n') {
                return true;
            }
        }
        return false;
    }

}
