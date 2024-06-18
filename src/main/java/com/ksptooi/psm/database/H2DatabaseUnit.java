package com.ksptooi.psm.database;

import com.ksptooi.guice.annotations.Unit;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

@Unit
public class H2DatabaseUnit {

    private static final Logger log = LoggerFactory.getLogger("H2Server");
    private volatile boolean isRunning = false;
    private Server dbServer = null;


    public synchronized void start() throws SQLException {
        start("1101");
    }

    public synchronized void start(String port) throws SQLException {
        if(!isRunning){
            dbServer = Server.createTcpServer("-tcpPort", port, "-tcpAllowOthers").start();
            log.info("已启动数据库实例 127.0.0.1:{}",port);
            isRunning = true;

            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                log.info("正在停止数据库实例");
                shutdown();
            }));

        }
    }

    public synchronized void shutdown(){
        if(isRunning){
            dbServer.shutdown();
        }
    }

}
