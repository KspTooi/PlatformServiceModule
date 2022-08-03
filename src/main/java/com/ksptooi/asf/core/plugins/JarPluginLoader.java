package com.ksptooi.asf.core.plugins;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.commons.JarFileFilter;
import com.ksptooi.asf.core.annatatiotion.PluginEntry;
import com.ksptooi.asf.core.entities.JarPlugin;
import com.ksptooi.asf.core.processor.Processor;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import com.ksptooi.asf.core.processor.ProcessorScanner;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class JarPluginLoader implements PluginLoader{

    private final Logger logger = LoggerFactory.getLogger(JarPluginLoader.class);

    private final Map<String, Plugin> loadedPlugins = new HashMap<>();

    @Inject
    private ProcessorDispatcher processorDispatcher;

    @Inject
    private ProcessorScanner scanner;

    public Map<String, Plugin> getPlugin(String directoryPath){

        logger.info("正在获取插件...");

        File dir = new File(directoryPath);

        if(!dir.exists()){
            logger.info("获取出错,路径\""+directoryPath+"\"不存在!");
            return new HashMap<>();
        }

        if(!dir.isDirectory()){
            logger.info("获取出错,路径\""+directoryPath+"\"不是文件夹!");
            return new HashMap<>();
        }

        File[] jarFiles = dir.listFiles(new JarFileFilter());

        if(jarFiles==null){
            return new HashMap<>();
        }

        Map<String, Plugin> pluginList = new HashMap<>();

        //加载jar
        for(File jar : jarFiles){

            try {

                URL url=jar.toURI().toURL();
                ClassLoader loader=new URLClassLoader(new URL[]{url});

                Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                        .addUrls(url).addClassLoaders(loader)
                );

                Set<Class<?>> entrySet = packageReflections.getTypesAnnotatedWith(PluginEntry.class);

                if(entrySet.size()!= 1){
                    logger.info("尝试获取"+jar.getName()+"时出错. 期望的Entry为1 当前为:"+entrySet.size());
                    continue;
                }

                Class<?> entry = entrySet.iterator().next();

                Plugin pluginEntry = (Plugin)entry.newInstance();
                logger.info("已获取:"+jar.getName()+ "::" + entry.getAnnotation(PluginEntry.class).name()+"["+entry.getAnnotation(PluginEntry.class).version()+"]");

                pluginList.put(entry.getAnnotation(PluginEntry.class).name(),pluginEntry);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return pluginList;
    }

    public void install(Map<String, Plugin> pluginMap){


        logger.info("正在加载插件...");

        for(Map.Entry<String, Plugin> item : pluginMap.entrySet()){
            logger.info("加载:"+item.getKey());
            ServiceFrame.injector.injectMembers(item.getValue());
            item.getValue().onEnabled();
            this.loadedPlugins.put(item.getKey(),item.getValue());
        }

    }

    @Override
    public List<JarPlugin> getJarPlugin(String directoryPath) {
        return this.getJarPlugin(new File(directoryPath));
    }

    @Override
    public List<JarPlugin> getJarPlugin(File dirFile) {

        logger.info("正在获取插件...");

        List<JarPlugin> retList = new ArrayList<>();

        if(!dirFile.exists()){
            logger.info("获取出错,路径\""+dirFile+"\"不存在!");
            return retList;
        }

        if(!dirFile.isDirectory()){
            logger.info("获取出错,路径\""+dirFile+"\"不是文件夹!");
            return retList;
        }

        File[] jarFiles = dirFile.listFiles(new JarFileFilter());

        if(jarFiles==null){
            return retList;
        }

        //加载jar
        for(File jar : jarFiles){

            try {

                URL url=jar.toURI().toURL();
                ClassLoader loader=new URLClassLoader(new URL[]{url});

                Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                        .addUrls(url).addClassLoaders(loader)
                );

                Set<Class<?>> entrySet = packageReflections.getTypesAnnotatedWith(PluginEntry.class);

                if(entrySet.size()!= 1){
                    logger.info("尝试获取"+jar.getName()+"时出错. 期望的Entry为1 当前为:"+entrySet.size());
                    continue;
                }

                Class<?> entry = entrySet.iterator().next();
                Plugin pluginEntry = (Plugin)entry.newInstance();
                String pluginName = entry.getAnnotation(PluginEntry.class).name();
                String pluginVersion = entry.getAnnotation(PluginEntry.class).version();

                JarPlugin jarPlugin = new JarPlugin();
                jarPlugin.setJarFile(jar);
                jarPlugin.setPluginName(pluginName);
                jarPlugin.setPluginVersion(pluginVersion);
                jarPlugin.setEntry(pluginEntry);
                jarPlugin.setProcessors(scanner.scan(url, loader));

                logger.info("已获取:{}-{}:{} 处理器数量:{}",jar.getName(),pluginName,pluginVersion,jarPlugin.getProcessors().size());
                retList.add(jarPlugin);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retList;
    }

    @Override
    public boolean install(JarPlugin jarPlugin) {

        Plugin entry = jarPlugin.getEntry();
        Map<String, Processor> processors = jarPlugin.getProcessors();

        logger.info("加载:{}[{}]",jarPlugin.getPluginName(),jarPlugin.getPluginVersion());

        ServiceFrame.injector.injectMembers(entry);
        entry.onEnabled();
        this.loadedPlugins.put(jarPlugin.getPluginName(),entry);

        for(Map.Entry<String,Processor> item:processors.entrySet()){

            logger.info("注册插件处理器:{}",item.getKey());
            this.processorDispatcher.register(item.getKey(),item.getValue());

        }

        return true;
    }

    @Override
    public void install(List<JarPlugin> jarPlugins) {

        for(JarPlugin item:jarPlugins){
            this.install(item);
        }

    }

}
