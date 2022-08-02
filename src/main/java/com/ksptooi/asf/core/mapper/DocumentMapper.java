package com.ksptooi.asf.core.mapper;

import com.ksptooi.asf.core.entities.Document;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentMapper {

    public List<Document> getDocumentList(@Param("where") Document where);

    public Document getDocumentById(Long id);

    public Document getDocumentByName(String name);

    public int insert(@Param("insert") Document document);

    public int update(@Param("update") Document document);

    public int remove(Long documentId);

}
