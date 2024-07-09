package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.CommandEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommandsMapper {

    public CommandEntity getByPattern(@Param("val")String val);

    public List<CommandEntity> queryMany(@Param("pattern")String pattern);

}
