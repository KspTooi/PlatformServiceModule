package com.ksptooi.psm.database.mybatis;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ksptooi.psm.bootstrap.BootOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ProviderDataSource implements Provider<DataSource> {

    @Inject
    private BootOptions options;

    @Override
    public DataSource get() {

        var dso = options.getDataSource();

        var hCfg = new HikariConfig();
        hCfg.setJdbcUrl(dso.getUrl());
        hCfg.setUsername(dso.getUsername());
        hCfg.setPassword(dso.getPassword());
        hCfg.setDriverClassName(dso.getDriverClassName());
        return new HikariDataSource(hCfg);
    }
}
