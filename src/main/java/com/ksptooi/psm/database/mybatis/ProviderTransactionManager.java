package com.ksptooi.psm.database.mybatis;

import com.google.inject.Provider;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class ProviderTransactionManager implements Provider<TransactionFactory> {
    @Override
    public TransactionFactory get() {
        return new JdbcTransactionFactory();
    }
}
