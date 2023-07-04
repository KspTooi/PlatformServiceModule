package com.ksptooi.uac.core.mapper;

import com.ksptooi.uac.core.entities.Document;
import org.apache.ibatis.annotations.Param;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.List;

public interface DocumentMapper {

    public List<Document> getDocumentList(@Param("where") Document where);

    public Document getDocumentById(Long id);

    public Document getDocumentByName(String name);

    public List<Document> getDocumentByType(@Param("type") String type);

    public int insert(@Param("insert") Document document);

    public int update(@Param("update") Document document);

    public int remove(Long documentId);

    public InputStream getBinaryData(@Param("documentId") Long documentId);

    public void updateBinaryData(@Param("documentId")Long documentId,@Param("stream") InputStream is);

}
