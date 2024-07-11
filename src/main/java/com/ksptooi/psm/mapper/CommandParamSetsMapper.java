package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.CommandParamSetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommandParamSetsMapper {

    public int insert(@Param("val") CommandParamSetEntity val);

    public int deleteById(@Param("val") Long id);

    public List<CommandParamSetEntity> getByCommandId(@Param("val") Long val);

}
