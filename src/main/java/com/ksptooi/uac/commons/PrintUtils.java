package com.ksptooi.uac.commons;

public class PrintUtils {

    public static void main(String[] args) {


        String[] th = new String[]{"THFDS1","TFFH2","THF4"};
        String[] th1 = new String[]{"TFSDFH1","TFSDFDSH2","THDFS4","TH5ADS"};

        String[][] tb = new String[][]{th,th,th,th,th,th,th,th1,th1};

        printTable(tb);

    }

    public static void printTable(String[][] table) {
        // Find out what the maximum number of columns is in any row
        int maxColumns = 0;
        for (int i = 0; i < table.length; i++) {
            maxColumns = Math.max(table[i].length, maxColumns);
        }

        // Find the maximum length of a string in each column
        int[] lengths = new int[maxColumns];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                lengths[j] = Math.max(table[i][j].length(), lengths[j]);
            }
        }

        // Generate a format string for each column
        String[] formats = new String[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
            formats[i] = "%1$" + lengths[i] + "s"
                    + (i + 1 == lengths.length ? "\n" : " ");
        }

        // Print 'em out
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.printf(formats[j], table[i][j]);
            }
        }
    }

}
