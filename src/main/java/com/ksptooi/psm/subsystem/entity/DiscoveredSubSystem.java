package com.ksptooi.psm.subsystem.entity;

import lombok.Data;

import java.io.File;

@Data
public class DiscoveredSubSystem {

    private File jarFile;
    private String name;
    private String version;
    private Class<?> entry;



}
