package com.ksptooi.uac.core.processor;

import java.net.URL;
import java.util.Map;

public interface ProcessorScanner {

    public Map<String,Processor> scan(String packagePath);

    public Map<String,Processor> scan(URL url);

    public Map<String,Processor> scan(URL url,ClassLoader classLoader);

}
