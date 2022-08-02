package com.ksptooi.asf.core.processor;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessorAnnotationScanner implements ProcessorScanner{


    @Override
    public Map<String, Processor> scan(String packagePath) {

        Reflections reflections = new Reflections(packagePath);

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(com.ksptooi.asf.core.annatatiotion.Processor.class);

        return this.getProcessorForClassSet(typesAnnotatedWith);
    }

    @Override
    public Map<String, Processor> scan(URL url) {

        ClassLoader loader=new URLClassLoader(new URL[]{url});

        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(loader)
        );

        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(com.ksptooi.asf.core.annatatiotion.Processor.class);

        return this.getProcessorForClassSet(typesAnnotatedWith);
    }


    @Override
    public Map<String, Processor> scan(URL url, ClassLoader classLoader) {

        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(classLoader)
        );

        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(com.ksptooi.asf.core.annatatiotion.Processor.class);

        return this.getProcessorForClassSet(typesAnnotatedWith);
    }

    private Map<String,Processor> getProcessorForClassSet(Set<Class<?>> classSet){

        if(classSet.size()<1){
            return new HashMap<>();
        }

        Map<String,Processor> retMap =  new HashMap<>();

        for(Class<?> item:classSet){

            try {

                Processor processor = (Processor) item.newInstance();
                retMap.put(item.getAnnotation(com.ksptooi.asf.core.annatatiotion.Processor.class).value(),processor);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retMap;
    }



}
