package com.ksptooi.asf.core.processor;

import org.reflections.Reflections;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessorAnnotationScanner implements ProcessorScanner{


    @Override
    public Map<String, Processor> scan(String packagePath) {

        Reflections reflections = new Reflections(packagePath);

        Set<Class<?>> processors = reflections.getTypesAnnotatedWith(com.ksptooi.asf.core.annatatiotion.Processor.class);

        if(processors.size()<1){
            return new HashMap<>();
        }

        Map<String,Processor> retMap =  new HashMap<>();

        while (processors.iterator().hasNext()) {

            Class<?> next = processors.iterator().next();

            try {

                Processor processor = (Processor) next.newInstance();
                retMap.put(next.getAnnotation(com.ksptooi.asf.core.annatatiotion.Processor.class).value(),processor);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retMap;
    }

    @Override
    public Map<String, Processor> scan(URL url) {
        return null;
    }

    @Override
    public Map<String, Processor> scan(URL url, ClassLoader classLoader) {
        return null;
    }

}
