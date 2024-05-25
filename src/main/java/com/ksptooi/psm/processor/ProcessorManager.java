package com.ksptooi.psm.processor;


import com.ksptooi.psm.mapper.ProcessorMapper;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于注册Processor
 * 用于接收请求字符串并将请求分发到Processor
 */
public class ProcessorManager {

    private static final Logger log = LoggerFactory.getLogger(ProcessorManager.class);

    @Inject
    private ProcessorMapper mapper;


    public void register(String name,Processor proc){



    }

}
