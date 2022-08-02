package com.ksptooi.asf.core.service;

import com.google.inject.Inject;
import com.ksptooi.asf.commons.IdWorker;
import com.ksptooi.asf.core.entities.Document;
import com.ksptooi.asf.core.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Inject
    private DocumentMapper mapper;

    public Document getDocumentById(Long id){
        return mapper.getDocumentById(id);
    }

    public Document getDocumentByName(String name){
        return mapper.getDocumentByName(name);
    }

    public List<Document> getDocumentList(Document document){
        return mapper.getDocumentList(document);
    }

    public boolean insert(Document in){

        if(this.getDocumentByName(in.getName())!=null){
            logger.info("文档名称已被占用:{}",in.getName());
            return false;
        }
        if(in.getName() == null || in.getName().trim().equals("")){
            logger.info("文档名称为空:{}",in.getName());
            return false;
        }

        in.setDocId(new IdWorker().nextId());
        in.setCreateTime(new Date());

        mapper.insert(in);
        return true;
    }



}
