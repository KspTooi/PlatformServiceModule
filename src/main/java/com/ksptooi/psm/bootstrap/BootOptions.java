package com.ksptooi.psm.bootstrap;

import lombok.Data;

@Data
public class BootOptions {
    private BootOptSshd sshd;
    private BootOptDataSource dataSource;
    private BootOptMybatis mybatis;
}
