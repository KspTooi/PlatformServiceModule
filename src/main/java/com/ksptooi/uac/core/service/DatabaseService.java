package com.ksptooi.uac.core.service;

import com.google.inject.Inject;
import com.ksptooi.uac.Application;
import com.ksptooi.uac.core.mapper.DatabaseMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;


public class DatabaseService {

    @Inject
    private DatabaseMapper mapper;


    public void initTableStructure(){

        Logger logger = Application.getLogger();


        Integer command = mapper.getTable("COMMAND");
        Integer document = mapper.getTable("DOCUMENT");

        if(command > 0 && document > 0){
            logger.info("数据库预检通过! 表结构完整.");
            return;
        }

        logger.info("正在初始化数据库..");

        if(command < 1){
            logger.info("构建表::COMMAND");
            mapper.createCommandTable();
        }

        if(document < 1){
            logger.info("构建表::DOCUMENT");
            mapper.createDocumentTable();
        }

        logger.info("数据库初始化完成.");

    }

}
