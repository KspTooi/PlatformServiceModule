package com.ksptooi.uac.core.plugins;

import com.ksptooi.uac.core.entities.InstalledPlugin;
import com.ksptooi.uac.core.entities.JarPlugin;

import java.io.File;
import java.util.List;

public interface PluginLoader {

    public List<JarPlugin> getJarPlugin(String directoryPath);

    public List<JarPlugin> getJarPlugin(File directoryFile);

    public boolean install(JarPlugin jarPlugin);

    public void install(List<JarPlugin> jarPlugins);

    public List<InstalledPlugin> getPluginList();

    public void remove(String pluginName);

}
