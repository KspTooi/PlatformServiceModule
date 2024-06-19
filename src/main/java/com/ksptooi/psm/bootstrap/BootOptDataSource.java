package com.ksptooi.psm.bootstrap;

import lombok.Data;

@Data
public class BootOptDataSource {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
