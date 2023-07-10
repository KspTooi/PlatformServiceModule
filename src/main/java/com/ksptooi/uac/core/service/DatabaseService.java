package com.ksptooi.uac.core.service;

import com.google.inject.Inject;
import com.ksptooi.uac.Application;
import com.ksptooi.uac.core.mapper.DatabaseMapper;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;


public class DatabaseService {

    @Inject
    private DatabaseMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    public void trim(){

        try{
            mapper.trim();
        }catch (Exception e){

        }

        logger.info("数据库压缩完成!");
    }

    public void export(Path path){

        if(Files.isDirectory(path)){
            logger.info("路径是文件夹:{}",path.toString());
            return;
        }

        if(Files.exists(path)){
            logger.info("文件已存在:{}",path.toString());
            return;
        }

        mapper.export(path.toString());

        if(Files.exists(path)){
            logger.info("已导出到:{}",path.toAbsolutePath().toString());
            return;
        }

        logger.info("导出数据库失败!");
    }

    @Transactional
    public void dbImport(Path path){

        if(!Files.exists(path)){
            logger.info("文件不存在:{}",path.toString());
            return;
        }

        mapper.dropAll();
        mapper.dbImport(path.toAbsolutePath().toString());
        this.initTableStructure();
        logger.info("执行完成");
    }


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
