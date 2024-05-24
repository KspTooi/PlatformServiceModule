package com.ksptooi.psm.utils;

public class VK {

    public static final int ENTER = 13;

    public static final int ESC = 27;
    public static final int TAB = 9;
    public static final int CTRL_C = 3;
    public static final int CTRL_A = 1;


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


    public static void print(char[] c,int len){

        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < len; i++) {
            sb.append((int)c[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ]");

        System.out.println(sb.toString());
    }

}
