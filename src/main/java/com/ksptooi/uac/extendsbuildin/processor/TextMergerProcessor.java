package com.ksptooi.uac.extendsbuildin.processor;

import com.google.gson.Gson;
import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.processor.ProcessorAdapter;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.entities.textmerger.TextMergerMetadata;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Processor("TextMergerProcessor")
public class TextMergerProcessor extends ProcessorAdapter {

    @Inject
    private DocumentService domService;

    private Gson gson = new Gson();

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "t",
                "t clear"
        };
    }

    @Override
    public void onInit() {


        if(!domService.hasDocument("TextMergerDocument")){
            Document dom = domService.createDocument("TextMergerDocument");
            TextMergerMetadata md = new TextMergerMetadata();
            md.setTextList(new ArrayList<>());
            dom.setMetadata(gson.toJson(md));
            domService.update(dom);
        }
    }

    @CommandMapping("t")
    public void t(){

        Document dom = domService.getDocumentByName("TextMergerDocument");
        TextMergerMetadata md = gson.fromJson(dom.getMetadata(), TextMergerMetadata.class);
        List<String> textList = md.getTextList();

        for (int i = 0; i < textList.size(); i++) {
            System.out.println(i+"."+textList.get(i));
        }
    }

    @CommandMapping("t")
    public void t(@Param("text")String text){
        Document dom = domService.getDocumentByName("TextMergerDocument");
        TextMergerMetadata md = gson.fromJson(dom.getMetadata(), TextMergerMetadata.class);
        md.getTextList().add(text);
        dom.setMetadata(gson.toJson(md));
        domService.update(dom);
    }

    @CommandMapping("t clear")
    public void tClear(){
        Document dom = domService.getDocumentByName("TextMergerDocument");
        TextMergerMetadata md = gson.fromJson(dom.getMetadata(), TextMergerMetadata.class);
        md.setTextList(new ArrayList<>());
        dom.setMetadata(gson.toJson(md));
        domService.update(dom);
    }

    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

    }

}
