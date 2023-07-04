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
import java.util.List;
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
                "cache get",
                "cache rm",
                "cache output",
                "c",
                "c list",
                "c get",
                "c rm",
                "c output",
        };
    }

    @CommandMapping({"cache","c"})
    public void cache(@Param("path")String filePath) {
        this.cache(UUID.randomUUID().toString(),filePath);
    }

    @CommandMapping({"cache","c"})
    public void cache(@Param("key") String key,@Param("path")String filePath){

        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            logger.info("路径:{} 不存在!",path);
            return;
        }

        Document dom = documentService.createDocument(key, "cache_storage");

        if(dom == null){
            return;
        }

        logger.info("正在分配空间..");

        boolean isRead = cacheService.readToDocument(path, dom);

        if(!isRead){
            logger.info("因未知原因缓存失败.");
            return;
        }

        documentService.update(dom);

        logger.info("已缓存 {} 字节",dom.getBinaryData().length);
        logger.info("资源标识:{}",dom.getName());
    }

    @CommandMapping({"cache list","c list"})
    public void cacheList(){
        cacheService.listAll();
    }


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        String name = preparedCommand.getName();

        if(name.equals("cache output") || name.equals("c output")){

            List<String> parameter = preparedCommand.getParameter();


            if(parameter.size() < 1){

                Path path = cacheService.getOutputPath();

                if(path == null){
                    logger.info("没有设定输出路径");
                    return;
                }

                logger.info("当前输出路径:{}",path.toString());
                return;
            }

            String paths = parameter.get(0);
            Path path = Paths.get(paths);

            if(!Files.exists(path)){
                logger.info("输出路径在文件系统上不存在. {}",paths);
                return;
            }

            if(!Files.isDirectory(path)){
                logger.info("输出路径不是一个文件夹. {}",paths);
                return;
            }

            cacheService.setOutputPath(paths);
        }


    }

}
