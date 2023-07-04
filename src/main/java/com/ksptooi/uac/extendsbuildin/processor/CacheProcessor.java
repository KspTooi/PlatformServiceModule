package com.ksptooi.uac.extendsbuildin.processor;

import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Processor("CacheProcessor")
public class CacheProcessor extends ProcessorAdapter {

    private final Logger logger = LoggerFactory.getLogger(CacheProcessor.class);

    @Inject
    private DocumentService documentService;

    @Inject
    private CacheService cacheService;


    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cache",
                "cache list",
                "cache get"
        };
    }

    @CommandMapping("cache")
    public void cache(@Param("path")String filePath) {

        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            logger.info("路径:{} 不存在!",path);
            return;
        }


        Document dom = documentService.createDocument(UUID.randomUUID().toString(), "cache_storage");
        logger.info("正在分配空间..");

        boolean isRead = cacheService.readToDocument(path, dom);

        if(!isRead){
            logger.info("因未知原因缓存失败.");
            return;
        }

        documentService.update(dom);

        logger.info("已缓存 {} 字节",dom.getBinaryData().length);
        logger.info("UUID:{}",dom.getName());
        logger.info("执行:cache 参数:path");
    }

    @CommandMapping("cache")
    public void cache(@Param("key") String key,@Param("path")String path){

        logger.info("执行:cache 参数值是:{} :: {}",key,path);
    }



    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {


    }

}
