package com.ksptooi;

import com.ksptooi.psm.bootstrap.BootOptions;
import com.ksptooi.psm.bootstrap.Bootstrap;
import com.ksptooi.psm.bootstrap.BootstrapException;

public class BootstrapTest {

    public static void main(String[] args) throws BootstrapException {


        var load = Bootstrap.load("bootstrap.yml");

        System.out.println(load);

    }

}
