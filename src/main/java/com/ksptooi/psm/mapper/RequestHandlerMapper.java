package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.RequestHandlerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RequestHandlerMapper {

    public RequestHandlerVo getByPattern(@Param("pattern") String name);

    public RequestHandlerVo getByPatternAndParamsCount(@Param("pattern") String name, @Param("pCount") int pCount);

    public int insert(@Param("val") RequestHandlerVo vo);

    public RequestHandlerVo getById(@Param("id")Long id);
}
