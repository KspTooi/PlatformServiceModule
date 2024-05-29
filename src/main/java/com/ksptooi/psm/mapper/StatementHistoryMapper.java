package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.StatementHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatementHistoryMapper {

    public int insert(@Param("val")StatementHistoryVo val);

    public List<StatementHistoryVo> getByUserId(@Param("val")Long val);

    public int delete(@Param("val")Long val);

    public List<StatementHistoryVo> getRecentByUserId(@Param("val")Long val);

}
