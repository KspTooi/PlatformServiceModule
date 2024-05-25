package com.ksptooi.psm.vk;

public class VK {

    public static final int ENTER = 13;

    public static final int ESC = 27;
    public static final int TAB = 9;
    public static final int CTRL_C = 3;
    public static final int CTRL_A = 1;
    public static final int BACKSPACE = 127;

    public static boolean IS_UP(char[] c,int len){
        if(len != 3){
            return false;
        }
        if(c[0] == 27 && c[1] == 91 && c[2] == 65){
            return true;
        }
        return false;
    }

    public static boolean IS_DOWN(char[] c,int len){
        if(len != 3){
            return false;
        }
        if(c[0] == 27 && c[1] == 91 && c[2] == 66){
            return true;
        }
        return false;
    }

    public static boolean IS_RIGHT(char[] c, int len) {
        if (len != 3) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 67;
    }

    public static boolean IS_LEFT(char[] c, int len) {
        if (len != 3) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 68;
    }

    public static boolean IS_INSERT(char[] c, int len) {
        if (len != 4) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 50 && c[3] == 126;
    }

    public static boolean IS_HOME(char[] c, int len) {
        if (len == 3 && c[0] == 27 && c[1] == 91 && c[2] == 72) {
            return true;
        }
        if (len == 4 && c[0] == 27 && c[1] == 91 && c[2] == 49 && c[3] == 126) {
            return true;
        }
        return false;
    }

    public static boolean IS_PAGE_UP(char[] c, int len) {
        if (len != 4) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 53 && c[3] == 126;
    }

    public static boolean IS_PAGE_DOWN(char[] c, int len) {
        if (len != 4) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 54 && c[3] == 126;
    }

    public static boolean IS_DELETE(char[] c, int len) {
        if (len != 4) {
            return false;
        }
        return c[0] == 27 && c[1] == 91 && c[2] == 51 && c[3] == 126;
    }

    public static boolean IS_END(char[] c, int len) {
        if (len == 3 && c[0] == 27 && c[1] == 91 && c[2] == 70) {
            return true;
        }
        if (len == 4 && c[0] == 27 && c[1] == 91 && c[2] == 52 && c[3] == 126) {
            return true;
        }
        return false;
    }

    /**
     * 是否为输入
     * @return
     */
    public static boolean IS_INPUT(char[] c, int len) {
        if (len == 0) {
            return false;
        }

        //在只有一个字符的情况下 第一个字符为CRLF 不能判定为输入 判定为回车
        if(len == 1 && ( c[0] == '\r' || c[0] == '\n' )){
            return false;
        }

        for (int i = 0; i < len; i++) {
            char ch = c[i];
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


    public static boolean CONTAINS_CRLF(char[] c, int len) {
        for (int i = 0; i < len; i++) {
            if (c[i] == '\r' || c[i] == '\n') {
                return true;
            }
        }
        return false;
    }


    public static void print(char[] c,int len){

        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < len; i++) {
            sb.append((int)c[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ]");

        System.out.println("VK_PRINT:: "+sb.toString());
    }

}
