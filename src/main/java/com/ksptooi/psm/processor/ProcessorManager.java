package com.ksptooi.psm.processor;


import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.ProcessorMapper;
import com.ksptooi.psm.mapper.RequestMapper;
import com.ksptooi.psm.modes.ProcessorVo;
import com.ksptooi.psm.processor.model.ActiveProcessor;
import jakarta.inject.Inject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.downgoon.snowflake.Snowflake;

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

    @Inject
    private RequestMapper reqMapper;

    @Inject
    private Snowflake snowflake;

    private final Map<String, ActiveProcessor> procMap = new HashMap<String,ActiveProcessor>();

    @Inject
    public ProcessorManager(ProcessorMapper mapper){
        this.mapper = mapper;
        shutdownAllProc();
    }

    public void shutdownAllProc(){
        procMap.clear();
        mapper.shutdown();
    }

    public void register(Map<String, Processor> procMap) {
        for(Map.Entry<String, Processor> item:procMap.entrySet()){
            this.register(item.getKey(),item.getValue());
        }
    }

    public void register(String name,Processor proc){

        ProcessorVo byName = mapper.getByName(name);

        if(byName != null && byName.getStatus() == 0){
            log.info("无法激活处理器:{} :: {}",name,proc.getClass().getName());
            return;
        }

        if(byName == null){
            log.info("添加处理器:{} :: {}",name,proc.getClass().getName());
            ProcessorVo insert = new ProcessorVo();
            insert.setId(snowflake.nextId());
            insert.setName(name);
            insert.setStatus(0);
            insert.setClassType(proc.getClass().getName());
            insert.setCreateTime(new Date());
            mapper.insert(insert);
            ActiveProcessor ap = new ActiveProcessor();
            ap.setProcId(insert.getId());
            ap.setProcName(name);
            ap.setProc(proc);
            procMap.put(name,ap);
            proc.activated();
            return;
        }

        log.info("激活处理器:{} :: {}",name,proc.getClass().getName());
        byName.setStatus(0);
        ActiveProcessor ap = new ActiveProcessor();
        ap.setProcId(byName.getId());
        ap.setProcName(name);
        ap.setProc(proc);
        procMap.put(name, ap);
        mapper.update(byName);
    }

    /**
     * 安装处理器指令
     */
    public void installProcRequest(){

        for (Map.Entry<String,ActiveProcessor> item : procMap.entrySet()){



        }

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
                retMap.put(item.getAnnotation(RequestProcessor.class).value(),processor);
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
