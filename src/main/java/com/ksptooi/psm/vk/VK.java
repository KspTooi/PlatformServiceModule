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

    public static final int[] A = {65};
    public static final int[] B = {66};
    public static final int[] C = {67};
    public static final int[] D = {68};
    public static final int[] E = {69};
    public static final int[] F = {70};
    public static final int[] G = {71};
    public static final int[] H = {72};
    public static final int[] I = {73};
    public static final int[] J = {74};
    public static final int[] K = {75};
    public static final int[] L = {76};
    public static final int[] M = {77};
    public static final int[] N = {78};
    public static final int[] O = {79};
    public static final int[] P = {80};
    public static final int[] Q = {81};
    public static final int[] R = {82};
    public static final int[] S = {83};
    public static final int[] T = {84};
    public static final int[] U = {85};
    public static final int[] V = {86};
    public static final int[] W = {87};
    public static final int[] X = {88};
    public static final int[] Y = {89};
    public static final int[] Z = {90};

    public static final int[] F1 = {27,79,80};
    public static final int[] F2 = {27,79,81};
    public static final int[] F3 = {27,79,82};
    public static final int[] F4 = {27,79,83};
    public static final int[] F5 = {27,91,49,53,126};
    public static final int[] F6 = {27,91,49,55,126};
    public static final int[] F7 = {27,91,49,56,126 };
    public static final int[] F8 = {27,91,49,57,126 };
    public static final int[] F9 = {27,91,50,48,126};
    public static final int[] F10 = {27,91,50,49,126};
    public static final int[] F11 = {27,91,50,51,126};
    public static final int[] F12 = {27,91,50,52,126};


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
