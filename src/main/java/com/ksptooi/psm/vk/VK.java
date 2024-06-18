package com.ksptooi.psm.vk;

public class VK {

    public static final int[] ENTER = {13};

    public static final int[] ESC = {27};
    public static final int[] TAB = {9};

    public static final int[] CTRL_A = {1};
    public static final int[] CTRL_B = {2};
    public static final int[] CTRL_C = {3};
    public static final int[] CTRL_D = {4};
    public static final int[] CTRL_E = {5};
    public static final int[] CTRL_F = {6};
    public static final int[] CTRL_G = {7};
    public static final int[] CTRL_H = {8};
    public static final int[] CTRL_I = {9};
    public static final int[] CTRL_J = {10};
    public static final int[] CTRL_K = {11};
    public static final int[] CTRL_L = {12};
    public static final int[] CTRL_M = {13};
    public static final int[] CTRL_N = {14};
    public static final int[] CTRL_O = {15};
    public static final int[] CTRL_P = {16};
    public static final int[] CTRL_Q = {17};
    public static final int[] CTRL_R = {18};
    public static final int[] CTRL_S = {19};
    public static final int[] CTRL_T = {20};
    public static final int[] CTRL_U = {21};
    public static final int[] CTRL_V = {22};
    public static final int[] CTRL_W = {23};
    public static final int[] CTRL_X = {24};
    public static final int[] CTRL_Y = {25};
    public static final int[] CTRL_Z = {26};



    public static final int[] BACKSPACE = {127};






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
