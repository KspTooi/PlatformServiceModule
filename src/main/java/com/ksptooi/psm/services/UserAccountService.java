package com.ksptooi.psm.services;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.UserVo;
import jakarta.inject.Inject;
import org.apache.commons.codec.digest.DigestUtils;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Date;

@Unit
public class UserAccountService {

    @Inject
    private UsersMapper userMapper;

    @Inject
    private Snowflake snowflake;


    public int getTotal(){
        return userMapper.count(null);
    }

    public UserVo getByAccount(String account){
        return userMapper.getByAccount(account);
    }

    public boolean createUser(String account,String password) throws Exception{

        UserVo query = new UserVo();
        query.setAccount(account);

        if(!userMapper.getList(query).isEmpty()){
            throw new Exception("账户名称重复.");
        }

        UserVo u = new UserVo();
        u.setUid(snowflake.nextId());
        u.setAccount(account);
        final String passwordCt = DigestUtils.sha512Hex(u.getUid() + password);
        u.setPassword(passwordCt);
        u.setStatus(0); // Active
        u.setLastLoginTime(new Date());
        u.setCreateTime(new Date());

        int insert = userMapper.insert(u);

        if(insert > 0){
            return true;
        }

        return false;
    }


}
