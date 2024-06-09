package com.ksptooi.psm.subsystem;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.exception.SubSystemInstallException;
import com.ksptooi.psm.subsystem.entity.DiscoveredSubSystem;
import com.ksptooi.uac.commons.JarFileFilter;
import com.ksptooi.uac.core.annatatiotion.PluginEntry;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Unit
public class SubSystemScanner {

    private static final Logger log = LoggerFactory.getLogger(SubSystemScanner.class);


    public List<DiscoveredSubSystem> scan(File dir){

        if(!dir.exists() || !dir.isDirectory()){
            return Collections.emptyList();
        }

        File[] jarFiles = dir.listFiles(new JarFileFilter());

        if(jarFiles==null || jarFiles.length < 1){
            return Collections.emptyList();
        }

        var ret = new ArrayList<DiscoveredSubSystem>();

        for (var jar : jarFiles){

            var jarFileName = jar.getName();

            try {

                var url = jar.toURI().toURL();
                var loader = new URLClassLoader(new URL[]{url});

                var packageReflections = new Reflections(new ConfigurationBuilder()
                        .addUrls(url).addClassLoaders(loader)
                );

                Set<Class<?>> entrySet = packageReflections.getTypesAnnotatedWith(SubSystemEntry.class);
                var entry = ensureCorrectEntry(jarFileName, entrySet);
                var anno = entry.getAnnotation(SubSystemEntry.class);

                var discovered = new DiscoveredSubSystem();
                discovered.setJarFile(jar);
                discovered.setName(anno.name());
                discovered.setVersion(anno.version());
                discovered.setEntry(entry);
                discovered.setClassLoader(loader);
                discovered.setReflections(packageReflections);

                log.info("发现可用的子系统 {}-{}:[{}]",anno.name(),anno.version(),jarFileName);
                ret.add(discovered);

            } catch (MalformedURLException | SubSystemInstallException e) {
                e.printStackTrace();
                continue;
            }
        }

        return ret;
    }


    public List<DiscoveredSubSystem> scan(String path){
        return scan(new File(path));
    }

    private Class<?> ensureCorrectEntry(String jarName,Set<Class<?>> entries){

        if(entries.isEmpty()){
            throw new SubSystemInstallException("The subsystem does not have entry. jar file name:"+jarName);
        }

        if(entries.size() != 1){
            throw new SubSystemInstallException("The subsystem has multiple entries the jar file name:"+jarName);
        }

        Class<?> next = entries.iterator().next();

        var annoEntry = next.getAnnotation(SubSystemEntry.class);
        var n = annoEntry.name();
        var v = annoEntry.version();

        if(StringUtils.isBlank(n)){
            throw new SubSystemInstallException("the subsystem is damaged. because %name% is blank in the entry.  jar file name:"+jarName);
        }
        if(StringUtils.isBlank(v)){
            throw new SubSystemInstallException("the subsystem is damaged. because %version% is blank in the entry.  jar file name:"+jarName);
        }

        return next;
    }

}
