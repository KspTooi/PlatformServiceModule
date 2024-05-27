package com.ksptooi.psm.processor;


import com.alibaba.fastjson.JSON;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.RequestDefinesMapper;
import com.ksptooi.psm.modes.RequestDefineVo;
import com.ksptooi.psm.processor.entity.ActiveProcessor;
import com.ksptooi.psm.processor.entity.ProcDefine;
import jakarta.inject.Inject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.downgoon.snowflake.Snowflake;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于注册Processor
 * 用于接收请求字符串并将请求分发到Processor
 */
@Unit
public class ProcessorManager {

    private static final Logger log = LoggerFactory.getLogger(ProcessorManager.class);

    @Inject
    private RequestDefinesMapper reqDefineMapper;

    @Inject
    private Snowflake snowflake;

    private final Map<String, ActiveProcessor> procMap = new ConcurrentHashMap<>();


    @Inject
    public ProcessorManager(){
        System.out.println("ProcessorManager 初始化");
    }


    public void register(Map<String, Processor> procMap) {
        for(Map.Entry<String, Processor> item:procMap.entrySet()){
            this.register(item.getKey(),item.getValue());
        }
    }

    /**
     * 注册处理器
     */
    public void register(String name,Processor proc){



    }

    /**
     * 安装处理器指令
     */
    public void installProcRequest(){

        for (Map.Entry<String,ActiveProcessor> item : procMap.entrySet()){

            List<ProcDefine> defines = item.getValue().getProcDefines();

            String procClassType = item.getValue().getProc().getClass().getName();

            for(ProcDefine def : defines){

                //获取数据库RequestDefine
                RequestDefineVo byName = reqDefineMapper.getByNameAndParameterCount(def.getPattern(),def.getParamCount());

                if(byName == null){

                    //ProcessorVo proc = mapper.getByName(def.getProcName());
//
                    //if(proc == null){
                    //    log.warn("无法为处理器 {} 安装指令 {}. 该处理器不存在于数据库中.",def.getProcName(),def.getPattern());
                    //    continue;
                    //}
//
                    //if(!proc.getClassType().equals(procClassType)){
                    //    log.warn("无法为处理器 {} 安装指令 {}. 处理器ClassType校验失败. 应为 {} 实际 {}",def.getProcName(),def.getPattern(),proc.getClassType(),procClassType);
                    //    continue;
                    //}
//
                    //log.info("安装处理器指令:{}->{}",def.getProcName(),def.getPattern());
                    //RequestDefineVo insert = new RequestDefineVo();
                    //insert.setId(snowflake.nextId());
                    //insert.setName(def.getPattern());
                    //insert.setParameterCount(def.getParamCount());
                    //insert.setParameters(JSON.toJSONString(def.getParams()));
                    //insert.setMetadata("");
                    //insert.setProcessorId(proc.getId());
                    //insert.setCreateTime(new Date());
                    //reqDefineMapper.insert(insert);
                }

            }

        }

    }


    /**
     * 向处理器转发请求
     * @param request
     * @return
     */
    public Thread forward(ProcRequest request){

        //解析Statement
/*        resolverRequest(request);

        //根据指令名+指令参数数量 找到数据库RequestDefine
        RequestDefineVo defVo = reqDefineMapper.getByNameAndParameterCount(request.getName(), request.getParameter().size());

        if(defVo == null){
            return null;
        }
        ProcessorVo procVo = mapper.getById(defVo.getProcessorId());
        if(procVo == null){
            return null;
        }
        //查找已加载的处理器
        ActiveProcessor activeProc = procMap.get(procVo.getName());
        if(!procVo.getClassType().equals(activeProc.getClassType())){
            log.warn("处理器已损坏,可尝试重新加载修复问题. 预计类型 {} 实际类型 {}",procVo.getClassType(),activeProc.getClassType());
            return null;
        }
        //匹配处理器中的Define
        ProcDefine targetDef = null;
        for(ProcDefine d : activeProc.getProcDefines()){
            if(d.getPattern().equals(request.getName()) && d.getParamCount() == request.getParameter().size()){
                targetDef = d;
            }
        }
        if(targetDef != null){
            //注入Define所需要的入参
            Object[] innerPar = { request };
            Object[] params = ProcTools.assemblyParams(targetDef.getMethod(), innerPar, request.getParameter());
            try {
                //执行处理器函数
                targetDef.getMethod().invoke(activeProc.getProc(),params);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        //如果没有找到注解 则执行默认处理器函数
        activeProc.getProc().newRequest(request);*/
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
