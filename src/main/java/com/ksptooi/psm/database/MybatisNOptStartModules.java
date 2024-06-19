package com.ksptooi.psm.database;

import com.ksptooi.psm.database.mybatis.ProviderDataSource;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

public class MybatisNOptStartModules extends MyBatisModule {

    public MybatisNOptStartModules(){
    }

    @Override
    protected void initialize() {

        bindDataSourceProviderType(ProviderDataSource.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);

    }
}
