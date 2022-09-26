package com.ksptooi.uac.core.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

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


}
