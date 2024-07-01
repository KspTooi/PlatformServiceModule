package com.ksptooi.inner;

import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.UserVo;
import com.ksptooi.psm.processor.RequestHandler;
import com.ksptooi.psm.processor.ServiceUnit;
import com.ksptooi.psm.processor.ShellRequest;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.uac.core.annatatiotion.Param;
import jakarta.inject.Inject;
import org.apache.commons.codec.digest.DigestUtils;
import xyz.downgoon.snowflake.Snowflake;
import java.util.Date;

@ServiceUnit("bundled:uac")
public class UserAccessControlUnit {

    @Inject
    private UsersMapper usersMapper;

    @Inject
    private Snowflake snowflake;


    @RequestHandler("whoami")
    public void whoami(ShellRequest request){
        var c = request.getCable().connect(ConnectMode.OUTPUT);
        c.w(request.getShell().getUserName());
    }

    @RequestHandler("uac user add")
    public void addUser(ShellRequest request,@Param("account")String account,@Param("password") String password){

        var c = request.getCable().connect(ConnectMode.OUTPUT);

        var byAccount = usersMapper.getByAccount(account);

        if(byAccount != null){
            c.w("用户:"+account+"已存在.");
            return;
        }

        var insert = new UserVo();
        insert.setUid(snowflake.nextId());
        insert.setAccount(account);
        var passwordCt = DigestUtils.sha512Hex(insert.getUid() + password);
        insert.setPassword(passwordCt);
        insert.setStatus(0);
        insert.setCreateTime(new Date());
        insert.setLastLoginTime(new Date());
        usersMapper.insert(insert);
        c.w("成功");
        return;
    }

    @RequestHandler("uac user delete")
    public void deleteUser(ShellRequest request,@Param("account")String account){

        var c = request.getCable().connect(ConnectMode.OUTPUT);
        var byAccount = usersMapper.getByAccount(account);

        if(byAccount == null){
            c.w("没有找到用户:"+account);
            return;
        }

        if(request.getShell().getUserName().equalsIgnoreCase(account)){
            c.w("无法移除当前用户");
            return;
        }

        usersMapper.delete(byAccount.getUid());
        c.w("成功");
        return;
    }

    @RequestHandler("uac users")
    public void listUsers(ShellRequest r){

        var c = r.getCable().connect(ConnectMode.OUTPUT);
        var list = usersMapper.getList(null);

        c.w("用户信息").nextLine().w("----------------").nextLine();

        c.w("用户名\tID").nextLine();
        for(var item : list){
            c.w(item.getAccount()).w("\t").w(item.getUid()).nextLine();
        }

    }

    public void listUsers(ShellRequest r,@Param("account")String account){

    }

}
