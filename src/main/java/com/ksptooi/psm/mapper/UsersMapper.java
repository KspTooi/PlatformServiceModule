package com.ksptooi.psm.mapper;

import com.ksptooi.psm.modes.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersMapper {

    public int insert(@Param("val") UserVo user);

    public int update(@Param("val") UserVo user);

    public int delete(@Param("val") Long id);

    public List<UserVo> getList(@Param("val") UserVo id);

    public int count(@Param("val") UserVo user);

}
