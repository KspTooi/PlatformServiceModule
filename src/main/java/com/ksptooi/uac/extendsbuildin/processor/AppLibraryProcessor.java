package com.ksptooi.uac.extendsbuildin.processor;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.enums.DocumentType;
import com.ksptooi.uac.extendsbuildin.service.AppLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Processor("build-in-AppLibraryProcessor")
public class AppLibraryProcessor extends ProcessorAdapter {

    private final Logger logger = LoggerFactory.getLogger(AppLibraryProcessor.class);

    @Inject
    private AppLibraryService service;

    @Inject
    private DocumentService documentService;

    @Override
    public void onInit() {

    }


    @Override
    public String[] defaultCommand() {
        return new String[]
                {
                 "lib add",
                 "lib a",
                 "lib remove",
                 "lib rm",
                 "lib list",
                 "lib l",
                 "lib scan",
                 "lib s",
                 "lib fix",
                 "lib f",
                };
    }

    @CommandMapping({"lib add","lib a"})
    public void addAppLibrary(@Param("name")String name,@Param("path")String path){

        Document document = documentService.createDocument(name, DocumentType.APP_LIB.getName());

        if(document==null){
            return;
        }

        Map<String,String> metadata = new HashMap<>();
        metadata.put("path",path);

        documentService.updateMetadata(name,JSON.toJSONString(metadata));
    }


    @CommandMapping({"lib list","lib l"})
    public void showLibList(){
        this.service.showAppLibraryList();
    }


    @CommandMapping({"lib remove","lib rm"})
    public void removeAppLibrary(@Param("name") String name){

        Document documentByName = documentService.getDocumentByName(name);

        if(documentByName==null){
            return;
        }

        documentService.removeById(documentByName.getDocId());
    }

    @CommandMapping({"lib scan","lib s"})
    public void scanAppLibrary(){
        this.service.getMissingApp();
    }

    @CommandMapping({"lib fix","lib f"})
    public void fixAppLibrary(){

    }


    @Override
    public void onCommand(CliCommand pCommand, Command command) {
    }




}
