package com.ksptooi.psm.mybatis;

import com.ksptooi.guice.annotations.Unit;
import jakarta.inject.Inject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@Unit
public class PostProcess {

    @Inject
    private SqlSessionFactory ssf = null;

    public void get(){

        var ssfb = new SqlSessionFactoryBuilder();


    }

}
