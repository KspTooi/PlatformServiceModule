package com.ksptooi.asf.core.processor;

import java.net.URL;
import java.util.Map;

public class ProcessorAnnotationScanner implements ProcessorScanner{


    @Override
    public Map<String, Processor> scan(String packagePath) {


        return null;
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
