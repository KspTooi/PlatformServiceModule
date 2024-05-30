package com.ksptooi.psm.vk;

public class VK {

    public static final int[] ENTER = {13};

    public static final int[] ESC = {27};
    public static final int[] TAB = {9};

    public static final int[] CTRL_A = {1};
    public static final int[] BACKSPACE = {127};


    public static final int[] CTRL_C = {3};
    public static final int[] UP = {27,91,65};
    public static final int[] DOWN = {27,91,66};
    public static final int[] RIGHT = {27,91,67};
    public static final int[] LEFT = {27,91,68};
    public static final int[] INSERT = {27,91,50,126};
    public static final int[] DELETE = {27,91,51,126};
    public static final int HOME = 10000;
    public static final int END = 10001;
    public static final int[] PAGE_UP = {27,91,53,126};
    public static final int[] PAGE_DOWN = {27,91,54,126};
    public static final int USER_INPUT = 20000;







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
