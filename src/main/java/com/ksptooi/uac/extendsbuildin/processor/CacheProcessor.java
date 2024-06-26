package com.ksptooi.uac.extendsbuildin.processor;

import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.mapper.DocumentMapper;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DatabaseService;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.service.CacheService;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Inject
    private DocumentMapper mapper;

    @Inject
    private DatabaseService dbService;


    @Override
    public String[] defaultCommand() {
        return new String[]{
                "c",
                "c list",
                "c get",
                "c rm",
                "c output"
        };
    }


    @CommandMapping({"c get"})
    public void cacheGet(@Param("key")String key){

        cacheService.saveAsDocument(Paths.get("E:\\Services"),UUID.randomUUID().toString());
        //cacheService.saveAsDocument(Paths.get("F:\\model"),UUID.randomUUID().toString());

    }

    @CommandMapping({"c rm"})
    public void cacheRemove(@Param("key")String key){

        Document dom = documentService.getDocumentByName(key);

        if(dom == null){
            logger.info("指定的资源不存在!");
            return;
        }

        documentService.removeById(dom.getDocId());
        dbService.trim();
    }


    @CommandMapping("c")
    public void cache(@Param("path")String filePath) {
        this.cache(UUID.randomUUID().toString(),filePath);
    }

    @CommandMapping("c")
    public void cache(@Param("key") String key,@Param("path")String filePath){

        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            logger.info("路径:{} 不存在!",path);
            return;
        }

        cacheService.saveAsDocument(path,key);
    }

    @CommandMapping("c list")
    public void cacheList(){
        cacheService.listAll();
    }


    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        String name = preparedCommand.getName();

        if(name.equals("c output")){

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
