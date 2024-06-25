package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.RequestHandlerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequestHandlerMapper {

    public List<RequestHandlerVo> getByPattern(@Param("pattern") String name);

    public RequestHandlerVo getByPatternAndParamsCount(@Param("pattern") String name, @Param("pCount") int pCount);

    public List<RequestHandlerVo> getRequestHandlers(@Param("pattern")String pattern,@Param("pCount")int pCount);

    public int insert(@Param("val") RequestHandlerVo vo);

    public RequestHandlerVo getById(@Param("id")Long id);

    public int deleteById(@Param("id")Long id);
}
