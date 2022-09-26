package com.ksptooi.uac.core.entities;

import com.ksptooi.uac.core.plugins.Plugin;
import com.ksptooi.uac.core.processor.Processor;

import java.io.File;
import java.util.Map;

public class JarPlugin {

    //插件文件
    private File jarFile;

    //插件名称
    private String pluginName;

    //插件版本
    private String pluginVersion;

    //插件入口点
    private Plugin entry;

    //处理器组
    private Map<String,Processor> processors;

    public File getJarFile() {
        return jarFile;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public Plugin getEntry() {
        return entry;
    }

    public void setEntry(Plugin entry) {
        this.entry = entry;
    }

    public Map<String, Processor> getProcessors() {
        return processors;
    }

    public void setProcessors(Map<String, Processor> processors) {
        this.processors = processors;
    }
}
