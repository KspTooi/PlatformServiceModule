package com.ksptooi.psm.database.mybatis;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import jakarta.inject.Inject;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;

@Singleton
public class EnvironmentProvider implements Provider<Environment> {

    @Inject
    private TransactionFactory transactionFactory;

    @Inject
    private DataSource dataSource;

    @Override
    public Environment get() {
        return new Environment("prod", this.transactionFactory, this.dataSource);
    }

}
