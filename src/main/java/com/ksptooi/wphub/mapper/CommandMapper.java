package com.ksptooi.wphub.mapper;

import com.ksptooi.wphub.entities.Command;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommandMapper {

    public int insert(Command command);


}
