package com.ksptooi.uac.extendsbuildin.service;


import com.google.gson.Gson;
import com.ksptooi.uac.commons.CliProgressBar;
import com.ksptooi.uac.commons.CommandLineTable;
import com.ksptooi.uac.commons.ZipCompress;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.entities.cache.CacheMetadata;
import com.ksptooi.uac.extendsbuildin.processor.CacheProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

public class CacheService {

    private final Logger logger = LoggerFactory.getLogger(CacheService.class);


    private final Gson gson = new Gson();

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Inject
    private DocumentService documentService;


    public void setOutputPath(String path){

        Document document = documentService.getDocumentByName("cache_output_path");

        if(document == null){
            document = documentService.createDocument("cache_output_path", "text_value");
        }

        document.setMetadata(path);
        documentService.update(document);
    }

    public Path getOutputPath(){

        Document dom = documentService.getDocumentByName("cache_output_path");

        if(dom == null){
            return null;
        }

        Path path = Paths.get(dom.getMetadata());

        if(!Files.exists(path)){
            logger.info("输出路径在文件系统上不存在. {}",dom.getMetadata());
            return null;
        }

        if(!Files.isDirectory(path)){
            logger.info("输出路径不是一个文件夹. {}",dom.getMetadata());
            return null;
        }

        return path;
    }


    /**
     * 将文件或文件夹读入Document
     * @param path 文件路径
     * @param document doc
     * @return 成功返回true 失败返回false
     */
    public boolean readToDocument(Path path, Document document){

        if(Files.isDirectory(path)){

            ZipCompress compress = new ZipCompress(path);

            logger.info("将文件夹转换为二进制数据.");

            try{
                document.setBinaryData(compress.compress());
            }catch (Exception e){
                return false;
            }

            //创建metadata
            CacheMetadata metadata = new CacheMetadata();
            metadata.setFileName(path.getFileName().toString());
            metadata.setPath(path.toString());
            metadata.setLength((long) document.getBinaryData().length);
            metadata.setDirectory(true);
            metadata.setCreateTime(new Date());
            metadata.setUpdateTime(new Date());
            document.setMetadata(new Gson().toJson(metadata));

            return true;
        }

        try {

            InputStream is = Files.newInputStream(path);

            byte[] read = new byte[1024*512];

            long count = 0L;

            long size = Files.size(path);

            while (true){

                int len = is.read(read);

                if(len < 1){
                    break;
                }
                count = count + len;

                CliProgressBar.updateProgressBar("处理中",count/1024/1024,size/1024/1024);
                document.appendBinaryData(read,len);
            }

            System.out.print("\r\n");

            //创建metadata
            CacheMetadata metadata = new CacheMetadata();
            metadata.setFileName(path.getFileName().toString());
            metadata.setPath(path.toString());
            metadata.setLength(size);
            metadata.setDirectory(false);
            metadata.setCreateTime(new Date());
            metadata.setUpdateTime(new Date());
            document.setMetadata(new Gson().toJson(metadata));

            is.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void listAll(){

        List<Document> caches = documentService.getDocumentByType("cache_storage");

        CommandLineTable clTable = new CommandLineTable();
        clTable.setHeaders("Key","FileName","Size","Directory","CrateTime");
        clTable.setShowVerticalLines(true);



        caches.forEach(item -> {

            //获取metadata
            CacheMetadata metadata = gson.fromJson(item.getMetadata(), CacheMetadata.class);
            clTable.addRow(item.getName(),metadata.getFileName(),metadata.getLength()/1024/1024+"MB", String.valueOf(metadata.isDirectory()),sdf.format(item.getCreateTime()));
        });

        clTable.print();

    }

}
