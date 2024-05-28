package com.ksptooi;

import org.h2.tools.Server;

public class H2Server {
    public static void main(String[] args) {
        try {

            // 启动TCP服务器
            //Server tcpServer = Server.createTcpServer("-tcpPort", "1101", "-tcpAllowOthers","-ifNotExists").start();
            Server tcpServer = Server.createTcpServer("-tcpPort", "1101", "-tcpAllowOthers").start();

            System.out.println("H2 TCP server started and listening on port 1101.");

            // 启动Web服务器（可选）
            Server webServer = Server.createWebServer("-webPort", "8082", "-webAllowOthers").start();
            System.out.println("H2 Web server started and listening on port 8082.");

            // 保持服务器运行
            System.out.println("Press [Enter] to stop the servers...");
            System.in.read();

            // 停止服务器
            webServer.stop();
            tcpServer.stop();
            System.out.println("H2 servers stopped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
