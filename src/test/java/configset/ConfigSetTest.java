package configset;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ksptooi.guice.compomentscan.ComponentsScanModule;
import com.ksptooi.psm.configset.ConfigSet;
import com.ksptooi.psm.mybatis.DatabaseModule;
import com.ksptooi.psm.shell.SshModules;
import org.junit.Before;
import org.junit.Test;

public class ConfigSetTest {

    public final static ComponentsScanModule csm = new ComponentsScanModule("com.ksptooi");
    public final static Injector injector = Guice.createInjector(new SshModules(), new DatabaseModule(),csm);

    public ConfigSet set;

    @Before
    public void before(){
        set = injector.getInstance(ConfigSet.class);
    }

    @Test
    public void getAndCreate(){
        String config = set.valOf("config","1");
        System.out.println(config);
    }




}
