package com.ksptooi.uac.extendsbuildin.processor;

import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;

@Processor("CacheProcessor")
public class CacheProcessor extends ProcessorAdapter {

    private final Logger logger = LoggerFactory.getLogger(CacheProcessor.class);

    @Inject
    private DocumentService documentService;


    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cache",
                "cache list",
                "cache get"
        };
    }

    @CommandMapping("cache")
    public void cache(@Param("path")String path) {

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
