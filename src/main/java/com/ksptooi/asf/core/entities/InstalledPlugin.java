package com.ksptooi.asf.core.entities;

import com.ksptooi.asf.core.plugins.Plugin;
import com.ksptooi.asf.core.processor.Processor;

import java.util.List;
import java.util.Map;

public class InstalledPlugin {

    //插件名字
    private String name;

    //插件版本
    private String version;

    //插件主入口
    private Plugin entry;

    //插件处理器组
    private List<String> processors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Plugin getEntry() {
        return entry;
    }

    public void setEntry(Plugin entry) {
        this.entry = entry;
    }

    public List<String> getProcessors() {
        return processors;
    }

    public void setProcessors(List<String> processors) {
        this.processors = processors;
    }
}
