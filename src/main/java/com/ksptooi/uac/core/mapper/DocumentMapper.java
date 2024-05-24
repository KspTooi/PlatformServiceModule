package com.ksptooi.uac.core.mapper;

import com.ksptooi.uac.core.entities.Document;
import org.apache.ibatis.annotations.Param;

import java.io.InputStream;
import java.util.List;

public interface DocumentMapper {

    public List<Document> getDocumentList(@Param("where") Document where);

    public Document getDocumentById(Long id);

    public Document getDocumentByName(String name);

    public List<Document> getDocumentByType(@Param("type") String type);

    public int insert(@Param("insert") Document document);

    public int update(@Param("update") Document document);

    public int remove(Long documentId);




    /**
     * 获取归档中二进制数据的大小
     * @param documentId 归档ID
     * @return 返回二进制数据的大小(byte) 若归档没有二进制数据则返回null
     */
    public Long getBinaryDataLength(@Param("documentId")Long documentId);

    /**
     * 查询归档中的二进制数据
     * @param documentId 归档ID
     * @return 返回用于读取二进制数据的字节流
     */
    public InputStream getBinaryData(@Param("documentId") Long documentId);

    /**
     * 更新归档中的二进制数据
     * @param documentId 归档ID
     * @param is 用于读取数据的字节流
     */
    public void updateBinaryData(@Param("documentId")Long documentId,@Param("stream") InputStream is);




}
