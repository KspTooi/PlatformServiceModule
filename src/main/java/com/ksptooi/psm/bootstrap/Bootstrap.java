package com.ksptooi.psm.bootstrap;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Bootstrap extends AbstractModule {


    private static final Logger log = LoggerFactory.getLogger("Boot");

    private File f;

    public Bootstrap(String path){
        this.f = new File(path);
    }

    @Override
    protected void configure() {
        try {
            var load = load(f);
            bind(BootOptions.class).toInstance(load);
        } catch (BootstrapException e) {
            throw new RuntimeException(e);
        }
    }

    public static BootOptions load(String path) throws BootstrapException{
        return load(new File(path));
    }
    public static BootOptions load(File f) throws BootstrapException{

        try {
            InputStream is = null;

            //优先从JAR包同一级别路径加载Boot
            if(f.exists()){
                log.info("Initialization BootOption From:{}",f.getAbsolutePath());
                is = Files.newInputStream(f.toPath());
            }
            //从ClassPath加载Boot
            if(is == null){
                log.info("Initialization BootOption From ClassPath:{}",f.getName());
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(f.getName());
            }

            var yml = new Yaml();
            var opt = yml.loadAs(is, BootOptions.class);
            ensureBootOptionsValid(opt);
            return opt;

        } catch (IOException e) {
            throw new BootstrapException(e);
        }
    }

    public static void ensureBootOptionsValid(BootOptions opt) throws BootstrapException{

        var sshd = opt.getSshd();
        var ds = opt.getDataSource();
        var mybatis = opt.getMybatis();

        if(sshd == null || ds == null || mybatis == null){
            throw new BootstrapException("Bootstrap file verification failed.[E0]");
        }

        var addrSplit = sshd.getServerAddr().split(":");

        if(addrSplit.length < 1){
            throw new BootstrapException("sshd.serverAddr must include port number");
        }

    }

}
