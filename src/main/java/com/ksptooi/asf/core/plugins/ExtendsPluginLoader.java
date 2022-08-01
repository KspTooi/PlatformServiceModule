package com.ksptooi.asf.core.plugins;

import com.ksptooi.asf.commons.JarFileFilter;
import com.ksptooi.asf.core.annatatiotion.PluginEntry;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExtendsPluginLoader {

    private final Logger logger = LoggerFactory.getLogger(ExtendsPluginLoader.class);

    public List<ExtendsPlugin> getPlugin(String directoryPath){

        logger.info("正在获取插件...");

        File dir = new File(directoryPath);

        if(!dir.exists()){
            logger.info("获取出错,路径\""+directoryPath+"\"不存在!");
            return new ArrayList<>();
        }

        if(!dir.isDirectory()){
            logger.info("获取出错,路径\""+directoryPath+"\"不是文件夹!");
            return new ArrayList<>();
        }

        dir.listFiles(new JarFileFilter());

        return null;
    }


    public void loadPlugin() throws Exception {




        File file=new File("C:/acu_system/plugins/my-plugin-1.0-SNAPSHOT.jar");

        URL url=file.toURI().toURL();
        ClassLoader loader=new URLClassLoader(new URL[]{url});
        Class<?> cls=loader.loadClass("com.ksptooi.plugin.MyPlugins");

        //InputStream resourceAsStream = loader.getResourceAsStream("plugin.yml");

        //BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));

        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(loader)
        );

        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(PluginEntry.class);

        System.out.println(typesAnnotatedWith.size());

        //File plugin = new File(resource.getFile());

        //System.out.println(br.readLine());

        Object instance = cls.newInstance();

        Method onEnabled = cls.getMethod("onEnabled");
        onEnabled.invoke(instance);
    }


}
