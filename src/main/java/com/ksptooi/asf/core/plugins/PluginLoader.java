package com.ksptooi.asf.core.plugins;

import com.ksptooi.asf.core.entities.JarPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface PluginLoader {

    public List<JarPlugin> getJarPlugin(String directoryPath);

    public List<JarPlugin> getJarPlugin(File directoryFile);

    public boolean install(JarPlugin jarPlugin);

    public void install(List<JarPlugin> jarPlugins);

    public Map<String, Plugin> getPluginList();

}
