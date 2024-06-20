package com.ksptooi.psm.database;

import com.google.inject.AbstractModule;
import org.mybatis.guice.XMLMyBatisModule;
import xyz.downgoon.snowflake.Snowflake;


public class MybatisXmlStartModules extends AbstractModule {

    private ClassLoader cl = null;

    public MybatisXmlStartModules(ClassLoader cl){
        this.cl = cl;
    }

    public MybatisXmlStartModules(){

    }

    @Override
    protected void configure() {
        install(new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                setEnvironmentId("prod");
                setClassPathResource("mybatis-config.xml");

                if(cl != null){
                    useResourceClassLoader(cl);
                }

            }
        });

        Snowflake snowflake = new Snowflake(1,1);
        bind(Snowflake.class).toInstance(snowflake);
        // 其他依赖注入配置
    }


}
