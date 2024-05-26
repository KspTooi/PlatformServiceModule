package com.ksptooi.psm.processor;


import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.ProcessorMapper;
import jakarta.inject.Inject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 用于注册Processor
 * 用于接收请求字符串并将请求分发到Processor
 */
@Unit
public class ProcessorManager {

    private static final Logger log = LoggerFactory.getLogger(ProcessorManager.class);

    @Inject
    private ProcessorMapper mapper;

    private final Map<String,Processor> procMap = new HashMap<String,Processor>();

    public void register(Map<String, Processor> procMap) {
        for(Map.Entry<String, Processor> item:procMap.entrySet()){
            this.register(item.getKey(),item.getValue());
        }
    }

    public void register(String name,Processor proc){

        if(procMap.containsKey(name)){
            log.info("注册Processor失败 - 名称被占用 {}",name);
            return;
        }

        procMap.put(name,proc);
        log.info("注册Processor:{}",name);
    }

    /**
     * 向处理器转发请求
     * @param request
     * @return
     */
    public Thread forward(ProcRequest request){
        PrintWriter pw = request.getPw();
        pw.println("[ProcessorManager] 处理Statement:"+request.getStatement());
        pw.flush();

        resolverRequest(request);

        pw.println("[ProcessorManager] 解析结果 Name:"+request.getName()+" Parameter:"+request.getParameter());
        pw.flush();
        return null;
    }


    /**
     * 从包路径中扫描并添加处理器
     */
    public void scanFromPackage(String packagePath){
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RequestProcessor.class);
        register(getProcessorForClassSet(typesAnnotatedWith));
    }

    /**
     * 从URL中扫描并添加处理器
     */
    public void scanFromURL(URL url) {
        ClassLoader loader = new URLClassLoader(new URL[]{url});
        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(loader)
        );
        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(RequestProcessor.class);
        register(getProcessorForClassSet(typesAnnotatedWith));
    }

    /**
     * 从插件中扫描并添加处理器
     */
    public void scanFromPlugins(URL url, ClassLoader classLoader) {
        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(classLoader)
        );
        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(RequestProcessor.class);
        register(getProcessorForClassSet(typesAnnotatedWith));
    }

    private Map<String, Processor> getProcessorForClassSet(Set<Class<?>> classSet){

        if(classSet.size()<1){
            return new HashMap<>();
        }

        Map<String, Processor> retMap =  new HashMap<>();

        for(Class<?> item:classSet){

            try {

                Processor processor = (Processor) item.newInstance();
                retMap.put(item.getAnnotation(com.ksptooi.uac.core.annatatiotion.Processor.class).value(),processor);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retMap;
    }

    /**
     * 解析请求语句
     * @param req
     * @return
     */
    private void resolverRequest(ProcRequest req){

        String statement = req.getStatement();

        String requestName = null;

        //预处理
        requestName = statement.trim();

        //解析参数
        String[] params = statement.split(">");

        //无参数
        if(params.length <= 1){
            req.setName(requestName);
            req.setParameter(new ArrayList<>());
            return;
        }

        //有参数
        List<String> paramList = new ArrayList<>();
        String param = params[1];

        String[] split = param.split(",");

        for(String item:split){

            if(item.trim().equals("")){
                continue;
            }

            paramList.add(item.trim());
        }

        req.setName(params[0]);
        req.setParameter(paramList);
    }



}