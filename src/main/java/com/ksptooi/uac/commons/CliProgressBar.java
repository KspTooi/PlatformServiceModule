package com.ksptooi.uac.commons;

public class CliProgressBar {

    public static void updateProgressBar(String title,long progress, long total) {

        int width = 50;

        long percent = progress * 100 / total;

        long completeLength = progress * width / total;

        StringBuilder sb = new StringBuilder();

        sb.append(title).append(" ");

        sb.append('[');

        for (int i = 0; i < completeLength; i++) {
            sb.append('=');
        }

        sb.append('>');

        for (long i = completeLength; i < width; i++) {
            sb.append(' ');
        }

        sb.append(']');

        sb.append(String.format(" %d%%", percent));

        sb.append(" ").append(progress).append(" of ").append(total);

        System.out.print("\r" + sb.toString());
    }
}
