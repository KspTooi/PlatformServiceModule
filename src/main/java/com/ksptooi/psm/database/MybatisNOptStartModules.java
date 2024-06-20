package com.ksptooi.psm.database;

import com.ksptooi.psm.database.mybatis.DataSourceProvider;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

public class MybatisNOptStartModules extends MyBatisModule {

    public MybatisNOptStartModules(){
    }

    @Override
    protected void initialize() {

        bindDataSourceProviderType(DataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);




    }




}
