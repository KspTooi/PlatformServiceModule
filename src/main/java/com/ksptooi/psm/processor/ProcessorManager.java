package com.ksptooi.psm.processor;


import com.alibaba.fastjson.JSON;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.RequestHandlerMapper;
import com.ksptooi.psm.modes.RequestHandlerVo;
import com.ksptooi.psm.processor.entity.ActiveProcessor;
import com.ksptooi.psm.processor.entity.HookTaskFinished;
import com.ksptooi.psm.processor.entity.ProcDefine;
import com.ksptooi.psm.processor.entity.ProcTask;
import com.ksptooi.psm.processor.event.BadRequestEvent;
import com.ksptooi.psm.processor.event.ProcEvent;
import com.ksptooi.Application;
import com.ksptooi.psm.shell.ShellUser;
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
    private RequestHandlerMapper requestHandlerMapper;

    @Inject
    private Snowflake snowflake;

    @Inject
    private TaskManager taskManager;

    private final Map<String, ActiveProcessor> procMap = new ConcurrentHashMap<>();

    private final Map<String, List<ProcDefine>> eventMap = new ConcurrentHashMap<>();

    @Inject
    public ProcessorManager(){
        System.out.println("ProcessorManager 初始化");
    }

    public void register(List<Object> procMap) {
        for(Object obj : procMap){
            register(obj);
        }
    }

    /**
     * 注册处理器
     */
    public void register(Object proc){

        String procName = ProcTools.getProcName(proc.getClass());
        String classType = proc.getClass().getName();

        try {

            List<ProcDefine> procDefine = ProcTools.getProcDefine(proc.getClass());

            if(procMap.containsKey(procName)){
                log.warn("无法注册处理器:{} 处理器名称冲突,当前已注册了一个相同名字的处理器.",procName);
                return;
            }

            ActiveProcessor p = new ActiveProcessor();
            p.setProcName(procName);
            p.setProc(proc);
            p.setClassType(classType);
            p.setProcDefines(procDefine);
            procMap.put(procName,p);
            log.info("已注册处理器:{} 包含{}个子系统",procName,procDefine.size());

            ProcDefine hook = DefineTools.getHook(ProcDefType.HOOK_ACTIVATED, procDefine);

            if(hook!=null){
                hook.getMethod().invoke(proc);
            }

            //注入内部组件
            Application.injector.injectMembers(proc);

        } catch (ProcDefineException e) {
            //e.printStackTrace();
            log.warn("无法注册处理器:{} - {} 因为处理器已损坏.",procName,classType);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 安装处理器指令
     */
    public void installRequestHandler(){

        for (Map.Entry<String,ActiveProcessor> item : procMap.entrySet()){

            List<ProcDefine> defines = item.getValue().getProcDefines();

            final String procName = item.getKey();
            final String procClassType = item.getValue().getProc().getClass().getName();

            for(ProcDefine def : defines){

                //不能安装Hook和事件
                if(!def.getDefType().equals(ProcDefType.REQ_HANDLER)){
                    continue;
                }
                //不能安装通配符执行器

                //获取数据库RequestDefine
                RequestHandlerVo byName = requestHandlerMapper.getByPatternAndParamsCount(def.getPattern(),def.getParamCount());

                //数据库已经注册过Handler
                if(byName!=null){
                    log.info("激活请求执行器 {}:{}({})",procName,byName.getPattern(),byName.getParamsCount());
                    continue;
                }

                RequestHandlerVo insert = new RequestHandlerVo();
                insert.setId(snowflake.nextId());
                insert.setPattern(def.getPattern());
                insert.setParams(JSON.toJSONString(def.getParams()));
                insert.setParamsCount(def.getParamCount());
                insert.setProcName(procName);
                insert.setProcClassType(procClassType);
                insert.setStatus(0);
                insert.setMetadata("");
                insert.setCreateTime(new Date());
                requestHandlerMapper.insert(insert);

                log.info("注册请求执行器 {}:{}({})",procName,def.getPattern(),def.getParamCount());
            }

        }

    }

    /**
     * 安装处理器事件
     */
    public void installEventHandler(){

        for (Map.Entry<String,ActiveProcessor> item : procMap.entrySet()){

            List<ProcDefine> procDefines = item.getValue().getProcDefines();

            for(ProcDefine def : procDefines){

                if(! def.getDefType().equals(ProcDefType.EVENT_HANDLER)){
                    continue;
                }

                //创建list
                List<ProcDefine> emList = eventMap.computeIfAbsent(def.getEventHandlerType(), k -> new ArrayList<>());
                emList.add(def);
                log.info("注册事件处理器 {}:{}({})..{}",def.getProcName(),def.getEventName(),def.getEventHandlerOrder(),def.getMethod().getName());
            }

        }

    }

    /**
     * 创建并发布事件
     */
    public ProcEvent forward(ProcEvent event){

        //查找已注册的事件处理器
        List<ProcDefine> defines = eventMap.get(event.getClass().getName());

        if(defines == null){
            return event;
        }

        Collections.sort(defines);

        for(ProcDefine def : defines){

            try {

                //获得处理器实例
                ActiveProcessor aProc = procMap.get(def.getProcName());
                def.getMethod().invoke(aProc.getProc(),event);

                if(event.isIntercepted()){
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.warn("执行事件时出现错误. 处理器:{} 事件名:{} 事件处理器:{}",def.getProcName(),def.getEventName(),def.getMethod().getName());
                continue;
            }

        }

        return event;
    }


    /**
     * 向处理器转发请求
     */
    public ProcTask forward(ProcRequest request,HookTaskFinished hook){

        resolverRequest(request);

        //查找数据库中的RequestHandler
        RequestHandlerVo requestHandlerVo = requestHandlerMapper.getByPatternAndParamsCount(request.getPattern(), request.getParams().size());

        if(requestHandlerVo == null){
            log.warn("无法处理请求:{} 无法从数据库查找到合适的Handler",request.getPattern());
            forward(new BadRequestEvent(request,BadRequestEvent.ERR_UNKNOWN_HANDLER));
            return null;
        }

        //根据数据库Handler查找内存中已加载的处理器
        ActiveProcessor aProc = procMap.get(requestHandlerVo.getProcName());

        if(!aProc.getClassType().equals(requestHandlerVo.getProcClassType())){
            log.warn("无法处理请求:{} 数据库与当前加载的处理器类型不一致. 数据库:{} 当前:{}",request.getPattern(),requestHandlerVo.getProcClassType(),aProc.getClassType());
            forward(new BadRequestEvent(request,BadRequestEvent.ERR_HANDLER_TYPE_INCONSISTENT));
            return null;
        }

        ShellUser user = request.getUser();


        //查找处理器中的Define
        ProcDefine define = DefineTools.getDefine(requestHandlerVo.getPattern(), requestHandlerVo.getParamsCount(), aProc.getProcDefines());


        //已找到对应Handler的Define
        if(define != null){

            //注入Define所需要的入参
            ProcTask procTask = new ProcTask(user, define.getMethod(), aProc,hook);
            Object[] innerPar = { request,procTask };
            Object[] params = ProcTools.assemblyParams(define.getMethod(), innerPar, request.getParams());
            procTask.setParams(params);

            //执行Define
            taskManager.commit(procTask);
            return procTask;
        }

        //没有找到对应的Define 尝试查找具有通配符的默认Define
        ProcDefine defaultDefine = DefineTools.getDefaultDefine(aProc.getProcDefines());

        if(defaultDefine != null){

            ProcTask procTask = new ProcTask(user, defaultDefine.getMethod(), aProc,hook);

            //注入Define所需要的入参
            Object[] innerPar = { request,procTask };
            Object[] params = ProcTools.assemblyParams(defaultDefine.getMethod(), innerPar, request.getParams());
            procTask.setParams(params);

            //执行Define
            taskManager.commit(procTask);
            return procTask;
        }

        //处理器中找不到任何Define
        forward(new BadRequestEvent(request,BadRequestEvent.ERR_CANNOT_ASSIGN_HANDLER));
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

    private List<Object> getProcessorForClassSet(Set<Class<?>> classSet){

        if(classSet.isEmpty()){
            return new ArrayList<>();
        }

        List<Object> ret = new ArrayList<>();

        for(Class<?> item:classSet){
            try {
                Object processor = item.getDeclaredConstructor().newInstance();
                ret.add(processor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
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
            req.setPattern(requestName);
            req.setParams(new ArrayList<>());
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

        req.setPattern(params[0]);
        req.setParams(paramList);
    }



}
