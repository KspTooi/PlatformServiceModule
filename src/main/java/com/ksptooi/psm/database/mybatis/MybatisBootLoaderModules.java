package com.ksptooi.psm.database.mybatis;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.AbstractMatcher;
import com.ksptooi.psm.bootstrap.BootOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.mappers.MapperProvider;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;
import xyz.downgoon.snowflake.Snowflake;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;

import static com.google.inject.matcher.Matchers.*;
import static com.google.inject.util.Providers.guicify;

public class MybatisBootLoaderModules extends AbstractModule {

    private BootOptions opt;

    public MybatisBootLoaderModules(BootOptions opt){
        this.opt = opt;
    }

    @Override
    protected void configure() {

        var dsOpt = opt.getDataSource();
        var mybatis = opt.getMybatis();

        var hCfg = new HikariConfig();
        hCfg.setJdbcUrl(dsOpt.getUrl());
        hCfg.setUsername(dsOpt.getUsername());
        hCfg.setPassword(dsOpt.getPassword());
        hCfg.setDriverClassName(dsOpt.getDriverClassName());

        var ds = new HikariDataSource(hCfg);
        var sfb = new SqlSessionFactoryBuilder();
        var cfg = new Configuration();

        cfg.setEnvironment(new Environment("prod",new JdbcTransactionFactory(),ds));
        cfg.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        cfg.setDatabaseId("H2");
        cfg.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.NONE);

        var ssf = sfb.build(cfg);

        bind(SqlSessionFactory.class).toInstance(ssf);
        bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
        bind(SqlSession.class).to(SqlSessionManager.class).in(Scopes.SINGLETON);

        //加载Mapper文件
        try {

            var resource = Thread.currentThread().getContextClassLoader().getResource("mapper/");

            if(resource == null){
                throw new RuntimeException("resource is null");
            }

            var file = new File(resource.getPath());
            var list = file.listFiles();

            if(list == null){
                throw new RuntimeException("resource is null");
            }

            for(var f : list){
                var xmb = new XMLMapperBuilder(new FileInputStream(f),cfg, f.getAbsolutePath(), cfg.getSqlFragments());
                xmb.parse();
            }

            var mappers = ssf.getConfiguration().getMapperRegistry().getMappers();

            for(var m : mappers){
                bindMapper(m);
            }

            Snowflake snowflake = new Snowflake(1,1);
            bind(Snowflake.class).toInstance(snowflake);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    protected void bindTransactionInterceptors() {
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        requestInjection(interceptor);
        bindInterceptor(any(), not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(annotatedWith(Transactional.class)),
                interceptor);
        bindInterceptor(annotatedWith(Transactional.class),
                not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(not(annotatedWith(Transactional.class))), interceptor);
    }


    protected static final AbstractMatcher<Method> DECLARED_BY_OBJECT = new AbstractMatcher<Method>() {
        @Override
        public boolean matches(Method method) {
            return method.getDeclaringClass() == Object.class;
        }
    };

    protected static final AbstractMatcher<Method> SYNTHETIC = new AbstractMatcher<Method>() {
        @Override
        public boolean matches(Method method) {
            return method.isSynthetic();
        }
    };

    final <T> void bindMapper(Class<T> mapperType) {
        bind(mapperType).toProvider(guicify(new MapperProvider<T>(mapperType))).in(Scopes.SINGLETON);
    }

}
