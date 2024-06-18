package com.ksptooi;

import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.UserVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MybatisColdStart {

    @Test
    public void coldStart() throws IOException {

        var hCfg = new HikariConfig();
        hCfg.setJdbcUrl("jdbc:h2:tcp://127.0.0.1:1101/./database;MAX_COMPACT_TIME=10000;CACHE_SIZE=0");
        hCfg.setUsername("");
        hCfg.setPassword("");
        hCfg.setDriverClassName("org.h2.Driver");
        var hds = new HikariDataSource(hCfg);
        var sfb = new SqlSessionFactoryBuilder();
        var cfg = new Configuration();

        var env = new Environment("prod",new JdbcTransactionFactory(),hds);
        cfg.setEnvironment(env);
        cfg.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        cfg.setDatabaseId("h2");
        cfg.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.NONE);

        var ssf = sfb.build(cfg);

        var resource = "mapper/UsersMapper.xml";
        var is = Resources.getResourceAsStream(resource);
        var xmb = new XMLMapperBuilder(is, cfg, resource, cfg.getSqlFragments());
        xmb.parse();

        var os = ssf.openSession(true);
        var mapper = os.getMapper(UsersMapper.class);

        var user = mapper.getByAccount("default");
        System.out.println(user);

    }

}
