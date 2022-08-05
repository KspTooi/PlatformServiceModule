package com.ksptooi.asf.extendsbuildin.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.commons.Metadata;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.Document;
import com.ksptooi.asf.core.service.CommandService;
import com.ksptooi.asf.core.service.DocumentService;
import com.ksptooi.asf.extendsbuildin.entities.PackLibrary;
import com.ksptooi.asf.extendsbuildin.entities.ApplicationData;
import com.ksptooi.asf.extendsbuildin.enums.BuildIn;
import com.ksptooi.asf.extendsbuildin.enums.DocumentType;
import org.apache.commons.codec.digest.DigestUtils;
import org.checkerframework.checker.units.qual.A;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//@Transactional
public class ApplicationService {

    private final Logger logger = LoggerFactory.getLogger(ApplicationService.class);


    @Inject
    private DocumentService service;

    @Inject
    private CommandService commandService;

    @Inject
    private DocumentService documentService;


    private final String packLibrayKey = "#pack_libray";


    //设置软件包目录
    public void setPackLib(String name,String path){

        PackLibrary library = new PackLibrary();
        List<PackLibrary> libraryList = new ArrayList<>();

        if(name == null){
            library.setName("软件包目录");
        }
        library.setPath(path);
        libraryList.add(library);

        Document document = service.getDocumentByName(this.packLibrayKey);

        if(document == null){
            document = service.createDocument(this.packLibrayKey);
        }

        document.setMetadata(new Gson().toJson(libraryList));
        service.update(document);
    }

    //显示软件包目录
    public void showPackLibs(){

        if(!service.hasDocument(this.packLibrayKey)){
            logger.info("当前没有设置软件包基准目录");
            return;
        }

        Document commandByName = service.getDocumentByName(this.packLibrayKey);

        List<PackLibrary> list = JSON.parseArray(commandByName.getMetadata(),PackLibrary.class);

        System.out.println("当前软件包目录");

        for(PackLibrary item:list){
            System.out.println(item.getName() + "--" + item.getPath());
        }

    }

    public void clearLibs(){

        if(!service.hasDocument(this.packLibrayKey)){
            logger.info("当前没有设置软件包基准目录");
            return;
        }

        Document commandByName = service.getDocumentByName(this.packLibrayKey);
        commandByName.setMetadata("[]");
        service.update(commandByName);

        logger.info("软件包基准目录已被清除");
    }











    public void saveAsDocument(Command app){
        
        ApplicationData appData = Metadata.asAppdata(app);

        Document document = documentService.createDocument(appData.getMd5(), DocumentType.APP_ARCHIVE.getName());
        document.setDescription("archived:"+appData.getPath());

        try {

            InputStream fis = Files.newInputStream(Paths.get(appData.getPath()));

            byte[] read = new byte[1024*500];

            while (true){
                int length = fis.read(read);

                if(length<1){
                    break;
                }

                document.appendBinaryData(read,length);
            }

            documentService.update(document);

            appData.setDocumentName(appData.getMd5());
            app.setMetadata(JSON.toJSONString(appData));
            commandService.update(app);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }












    //自动安装包
    public Command appInstall(String name, String path){

        if(commandService.hasCommand(name)){
            logger.info("应用安装失败,指令\""+name+"\"已被占用");
            return null;
        }

        boolean exists = Files.exists(Paths.get(path));
        File file = new File(path);

        if(!exists){
            logger.info("应用安装失败,提供的Path不正确! \""+path+"\"");
            return null;
        }


        ApplicationData data = new ApplicationData();
        data.setPath(path);
        data.setFileName(file.getName());
        data.setDirectory(file.isDirectory());
        data.setLength(file.length());

        if(!file.isDirectory()){
            try {
                FileInputStream fis = new FileInputStream(file);
                data.setMd5(DigestUtils.md5Hex(fis));
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Command insert = new Command();
        insert.setName(name);
        insert.setExecutorName(BuildIn.APP_RUNNER.getProcessorName());
        insert.setMetadata(new Gson().toJson(data));
        commandService.insert(insert);

        logger.info("应用安装完成,指令为: \""+name+"\"");
        return insert;
    }

    //移除软件包
    public void appRemove(String name){

        Command commandByName = commandService.getCommandByName(name);

        if(commandByName == null){
            logger.info("软件包移除失败,指令\""+name+"\"不存在!");
            return;
        }

        ApplicationData appData = JSON.parseObject(commandByName.getMetadata(), ApplicationData.class);

        if(appData.getDocumentName()!=null){

            Document documentByName = documentService.getDocumentByName(appData.getDocumentName());

            if(documentByName!=null){
                documentService.removeById(documentByName.getDocId());
                logger.info("删除关联归档:{}",appData.getDocumentName());
            }
        }

        commandService.removeById(commandByName.getCmdId()+"");
        logger.info("软件包\""+name+"\"移除成功!");
    }

    //显示所有已安装的应用
    public void appShow(){

        List<Command> apps = commandService.getCommandByProcessorName(BuildIn.APP_RUNNER.getProcessorName());

        CommandLineTable cliTable = new CommandLineTable();
        cliTable.setShowVerticalLines(true);
        cliTable.setHeaders("Name","Path");

        apps.forEach(item->{
            cliTable.addRow(item.getName(),JSON.parseObject(item.getMetadata()).getString("path"));
        });

        cliTable.print();
    }


}
