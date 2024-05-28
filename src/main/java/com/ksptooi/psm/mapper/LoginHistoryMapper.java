package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginHistoryMapper {

    public int insert(@Param("val")UserVo val);


}
