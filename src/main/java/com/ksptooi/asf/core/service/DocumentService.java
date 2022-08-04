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

    private final IdWorker idWorker = new IdWorker();

    public Document getDocumentById(Long id){
        return mapper.getDocumentById(id);
    }

    public Document getDocumentByName(String name){
        return mapper.getDocumentByName(name);
    }

    public List<Document> getDocumentList(Document document){
        return mapper.getDocumentList(document);
    }

    public List<Document> getDocumentByType(String documentType){
        return mapper.getDocumentByType(documentType);
    }

    public Document createDocument(String name){
        return this.createDocument(name,null);
    }

    public Document createDocument(String name,String type){

        Document dom = new Document();
        dom.setDocId(this.idWorker.nextId());
        dom.setName(name);
        dom.setDomType(type);
        dom.setMetadata(null);
        dom.setBinaryData(null);
        dom.setDescription("document");
        dom.setCreateTime(new Date());

        boolean insert = this.insert(dom);

        if(!insert){
            return null;
        }

        return dom;
    }

    public boolean hasDocument(String name){

        Document documentByName = this.getDocumentByName(name);

        if(documentByName == null){
            return false;
        }

        return true;
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

    public boolean update(Document in){

        Document update = this.getDocumentById(in.getDocId());

        if(update==null){
            logger.info("修改失败,文档不存在.ID:{}",in.getDocId());
            return false;
        }

        update.setMetadata(in.getMetadata());
        update.setBinaryData(in.getBinaryData());
        update.setDescription(in.getDescription());
        mapper.update(in);
        return true;
    }

    public boolean removeById(Long id){
        mapper.remove(id);
        return true;
    }

    public String queryMetadata(String inDocName){

        Document documentByName = this.getDocumentByName(inDocName);

        if(documentByName == null){
            return null;
        }

        return documentByName.getMetadata();
    }

    public boolean updateMetadata(String inDomName,String metadata){

        Document documentByName = this.getDocumentByName(inDomName);

        if(documentByName==null){
            return false;
        }

        documentByName.setMetadata(metadata);
        this.update(documentByName);
        return true;
    }



}
