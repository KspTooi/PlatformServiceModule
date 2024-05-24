package com.ksptooi.uac.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DatabaseMapper {

    /**
     * 使用information_schema检查表是否存在
     * @param tableName
     * @return
     */
    public Integer getTable(@Param("tableName")String tableName);

    public void createCommandTable();

    public void createDocumentTable();

    public void trim();

    public void export(@Param("path") String path);

    public void dbImport(@Param("path")String path);

    public void dropAll();


}
