package com.ksptooi.psm.subsystem.entity;

import com.ksptooi.uac.commons.ReflectUtils;
import lombok.Data;
import org.reflections.Reflections;

import java.io.File;

@Data
public class DiscoveredSubSystem {

    private File jarFile;
    private String name;
    private String version;
    private Class<?> entry;
    private ClassLoader classLoader;
    private Reflections reflections;

}
