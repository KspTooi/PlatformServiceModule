package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.ProcessorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcessorMapper {

    public ProcessorVo getByName(@Param("name")String name);

    public int insert(ProcessorVo vo);

}
