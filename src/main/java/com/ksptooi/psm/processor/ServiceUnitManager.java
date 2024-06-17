package com.ksptooi.psm.processor;


import com.alibaba.fastjson.JSON;
import com.google.inject.Injector;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.RequestHandlerMapper;
import com.ksptooi.psm.modes.RequestHandlerVo;
import com.ksptooi.psm.processor.entity.*;
import com.ksptooi.psm.processor.entity.Process;
import com.ksptooi.psm.processor.event.BadRequestEvent;
import com.ksptooi.Application;
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
public class ServiceUnitManager {

    private static final Logger log = LoggerFactory.getLogger(ServiceUnitManager.class);

    @Inject
    private RequestHandlerMapper requestHandlerMapper;

    @Inject
    private Snowflake snowflake;

    @Inject
    private TaskManager taskManager;

    @Inject
    private EventSchedule eventSchedule;

    private final static Map<String, ActivatedSrvUnit> procMap = new ConcurrentHashMap<>();



    public void register(Injector i){



    }



    public void register(List<Object> procMap) {
        for(Object obj : procMap){
            register(obj);
        }
    }

    /**
     * 注册服务单元
     */
    public void register(Object proc){

        String procName = ServiceUnits.getSrvUnitName(proc.getClass());
        String classType = proc.getClass().getName();

        try {

            List<SrvDefine> srvDefine = ServiceUnits.getSrvUnits(proc.getClass());

            if(procMap.containsKey(procName)){
                log.warn("无法注册服务单元:{} 服务单元名称冲突,当前已注册了一个相同名字的服务单元.",procName);
                return;
            }

            ActivatedSrvUnit p = new ActivatedSrvUnit();
            p.setSrvUnitName(procName);
            p.setSrvUnit(proc);
            p.setClassType(classType);
            p.setSrvDefines(srvDefine);
            p.setRequestHandlerInstalled(false);
            p.setEventHandlerInstalled(false);
            procMap.put(procName,p);
            log.info("已注册服务单元:{} 包含{}个内部构件",procName, srvDefine.size());

            SrvDefine hook = Defines.getHook(SrvDefType.HOOK_ACTIVATED, srvDefine);

            if(hook!=null){
                hook.getMethod().invoke(proc);
            }

            //注入内部组件
            Application.injector.injectMembers(proc);

        } catch (ServiceDefinitionException e) {
            e.printStackTrace();
            log.warn("无法注册服务单元:{} - {} 因为服务单元已损坏.",procName,classType);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 安装服务单元指令
     */
    public void installRequestHandler(){

        for (Map.Entry<String, ActivatedSrvUnit> item : procMap.entrySet()){

            //注册的服务单元已安装过请求处理器
            if(item.getValue().isRequestHandlerInstalled()){
                continue;
            }

            List<SrvDefine> defines = item.getValue().getSrvDefines();

            final String procName = item.getKey();
            final String procClassType = item.getValue().getSrvUnit().getClass().getName();

            for(SrvDefine def : defines){

                //不能安装Hook和事件
                if(!def.getDefType().equals(SrvDefType.REQ_HANDLER)){
                    continue;
                }
                //不能安装通配符执行器

                //获取数据库RequestDefine
                RequestHandlerVo byName = requestHandlerMapper.getByPatternAndParamsCount(def.getPattern(),def.getParamCount());

                //数据库已经注册过Handler
                if(byName!=null){

                    //数据库的请求处理器类型与当前服务单元类型不一致
                    if(!byName.getSrvUnitClassType().equals(procClassType)){
                        requestHandlerMapper.deleteById(byName.getId());
                        log.info("移除请求处理器 {}:{}",byName.getSrvUnitName(),byName.getSrvUnitClassType());
                    }else {
                        log.info("激活请求处理器 {}:{}({})",procName,byName.getPattern(),byName.getParamsCount());
                        continue;
                    }
                }

                RequestHandlerVo insert = new RequestHandlerVo();
                insert.setId(snowflake.nextId());
                insert.setPattern(def.getPattern());
                insert.setParams(JSON.toJSONString(def.getParams()));
                insert.setParamsCount(def.getParamCount());
                insert.setSrvUnitName(procName);
                insert.setSrvUnitClassType(procClassType);
                insert.setStatus(0);
                insert.setMetadata("");
                insert.setCreateTime(new Date());
                requestHandlerMapper.insert(insert);

                log.info("注册请求执行器 {}:{}({})",procName,def.getPattern(),def.getParamCount());
            }

            item.getValue().setRequestHandlerInstalled(true);

        }

    }

    /**
     * 安装服务单元事件
     */
    public void installEventHandler(){

        for (Map.Entry<String, ActivatedSrvUnit> item : procMap.entrySet()){

            //注册的服务单元已安装过事件处理器
            if(item.getValue().isEventHandlerInstalled()){
                continue;
            }

            List<SrvDefine> srvDefines = item.getValue().getSrvDefines();

            for(SrvDefine def : srvDefines){

                if(! def.getDefType().equals(SrvDefType.EVENT_HANDLER)){
                    continue;
                }

                //不安装进程内事件
                if(! def.isGlobalEventHandler()){
                    continue;
                }

                eventSchedule.register(def);
            }

            item.getValue().setEventHandlerInstalled(true);

        }

    }

    /**
     * 向服务单元转发请求
     */
    public Process forward(ShellRequest request, HookTaskFinished hook){

        resolverRequest(request);

        //查找数据库中的RequestHandler
        RequestHandlerVo requestHandlerVo = requestHandlerMapper.getByPatternAndParamsCount(request.getPattern(), request.getParams().size());

        if(requestHandlerVo == null){
            log.warn("无法处理请求:{} 无法从数据库查找到合适的Handler",request.getPattern());
            eventSchedule.forward(new BadRequestEvent(request, BadRequestEvent.ERR_UNKNOWN_HANDLER));
            return null;
        }

        //根据数据库Handler查找内存中已加载的服务单元
        ActivatedSrvUnit aProc = procMap.get(requestHandlerVo.getSrvUnitName());

        if(aProc == null){
            eventSchedule.forward(new BadRequestEvent(request,BadRequestEvent.ERR_UNKNOWN_HANDLER));
            return null;
        }

        if(!aProc.getClassType().equals(requestHandlerVo.getSrvUnitClassType())){
            log.warn("无法处理请求:{} 数据库与当前加载的服务单元类型不一致. 数据库:{} 当前:{}",request.getPattern(),requestHandlerVo.getSrvUnitClassType(),aProc.getClassType());
            eventSchedule.forward(new BadRequestEvent(request,BadRequestEvent.ERR_HANDLER_TYPE_INCONSISTENT));
            return null;
        }

        //ShellInstance user = request.getShellInstance();

        //查找服务单元中的Define
        var procDef = Defines.getDefine(requestHandlerVo.getPattern(), requestHandlerVo.getParamsCount(), aProc.getSrvDefines());

        //没有找到映射Define 尝试查找具有通配符的默认Define
        if(procDef == null){
            procDef = Defines.getDefaultDefine(aProc.getSrvDefines());
        }

        if(procDef == null){
            //服务单元中找不到任何Define
            eventSchedule.forward(new BadRequestEvent(new ShellRequest(request),BadRequestEvent.ERR_CANNOT_ASSIGN_HANDLER));
        }

        //注入请求元数据
        request.setMetadata(requestHandlerVo.getMetadata());

        //注入Define所需要的入参
        var t = new Process();
        t.setTaskName(procDef.getMethod().getName());
        t.setRequest(request);
        t.setServiceUnit(aProc);
        t.setTarget(procDef.getMethod());
        t.setInjectParams(ServiceUnits.assemblyParams(procDef.getMethod(),request.getParams(),request,t,taskManager));
        t.setFinishHook(hook);
        t.setTaskManager(taskManager);

        //执行Define
        taskManager.commit(t);
        return t;
    }


    /**
     * 从包路径中扫描并添加服务单元
     */
    public void scanFromPackage(String packagePath){
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ServiceUnit.class);
        register(getProcessorForClassSet(typesAnnotatedWith));
    }

    /**
     * 从URL中扫描并添加服务单元
     */
    public void scanFromURL(URL url) {
        ClassLoader loader = new URLClassLoader(new URL[]{url});
        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(loader)
        );
        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(ServiceUnit.class);
        register(getProcessorForClassSet(typesAnnotatedWith));
    }

    /**
     * 从插件中扫描并添加服务单元
     */
    public void scanFromPlugins(URL url, ClassLoader classLoader) {
        Reflections packageReflections = new Reflections(new ConfigurationBuilder()
                .addUrls(url).addClassLoaders(classLoader)
        );
        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(ServiceUnit.class);
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
    private void resolverRequest(ShellRequest req){

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

    public static ActivatedSrvUnit getProcessor(String procName){
        return procMap.get(procName);
    }



}
