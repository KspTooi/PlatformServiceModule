package com.ksptooi.asf.core.mapper;

import com.ksptooi.asf.core.entities.Command;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommandMapper {

    public int insert(Command command);

    public Command getCommandById(String id);

    public List<Command> getCommandList(@Param("where") Command where);

    public int update(@Param("update")Command command);

    public int removeById(String id);

}
