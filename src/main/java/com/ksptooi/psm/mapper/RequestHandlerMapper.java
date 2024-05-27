package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.RequestHandlerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RequestHandlerMapper {

    public RequestHandlerVo getByName(@Param("name") String name);

    public RequestHandlerVo getByNameAndParameterCount(@Param("name") String name, @Param("pCount") int pCount);

    public int insert(@Param("val") RequestHandlerVo vo);
}
