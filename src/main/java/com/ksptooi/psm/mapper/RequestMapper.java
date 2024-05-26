package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.RequestVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RequestMapper {

    public RequestVo getByName(@Param("name") String name);

    public int insert(@Param("val") RequestVo vo);
}
