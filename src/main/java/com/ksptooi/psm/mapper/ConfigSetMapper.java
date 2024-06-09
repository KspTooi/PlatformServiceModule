package com.ksptooi.psm.mapper;

import com.ksptooi.psm.configset.ConfigSetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigSetMapper {

    public List<ConfigSetVo> getByKey(@Param("key") String key);

    public int insert(@Param("val") ConfigSetVo val);

}
