package com.ksptooi.psm.subsystem.entity;

import com.google.inject.Injector;
import com.ksptooi.psm.subsystem.SubSystem;
import lombok.Data;
import org.reflections.Reflections;

import java.io.File;
import java.util.List;

@Data
public class ActivatedSubSystem {
    private File jarFile;
    private String name;
    private String version;
    private SubSystem entry;
    private ClassLoader classLoader;
    private Reflections reflections;
    private List<Class<?>> processorDefine;
    private Injector injector;
}
