package com.ksptooi.uac;

public class ProgressBarExample {
    public static void main(String[] args) {
        int total = 100;
        
        for (int progress = 0; progress <= total; progress++) {
            updateProgressBar(progress, total);
            
            // 模拟一些处理时间
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n进度条完成！");
    }
    
    public static void updateProgressBar(int progress, int total) {
        int width = 50;
        int percent = progress * 100 / total;
        int completeLength = progress * width / total;
        
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < completeLength; i++) {
            sb.append('=');
        }
        sb.append('>');
        for (int i = completeLength; i < width; i++) {
            sb.append(' ');
        }
        sb.append(']');
        sb.append(String.format(" %d%%", percent));
        
        System.out.print("\r" + sb.toString());
    }
}
