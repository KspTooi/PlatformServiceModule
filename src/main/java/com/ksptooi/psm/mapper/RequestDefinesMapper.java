package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.RequestDefineVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RequestDefinesMapper {

    public RequestDefineVo getByName(@Param("name") String name);

    public int insert(@Param("val") RequestDefineVo vo);
}
