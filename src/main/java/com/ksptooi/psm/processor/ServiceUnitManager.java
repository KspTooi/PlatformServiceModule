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
import com.ksptooi.psm.utils.RefTools;
import com.ksptooi.psm.utils.aio.AdvInputOutputCable;
import com.ksptooi.psm.utils.aio.color.RedDye;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.downgoon.snowflake.Snowflake;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于注册Processor
 * 用于接收请求字符串并将请求分发到Processor
 */
@Unit
public class ServiceUnitManager {

    private static final Logger log = LoggerFactory.getLogger("SrvUnitMgr");

    @Inject
    private RequestHandlerMapper requestHandlerMapper;

    @Inject
    private Snowflake snowflake;

    @Inject
    private TaskManager taskManager;

    @Inject
    private EventSchedule eventSchedule;

    @Inject
    private StatementResolver statementResolver;


    private final static Map<String, ActivatedSrvUnit> procMap = new ConcurrentHashMap<>();


    /**
     * 从IOC容器中注册可用的服务单元(ServiceUnit)
     * @param i IOC容器
     * @throws ServiceUnitRegException 服务单元注册失败后执行回滚并抛出该异常
     */
    public void register(Injector i) throws ServiceUnitRegException{

        var bk = i.getBindings().keySet();
        var preparingSubmit = new HashMap<String,ActivatedSrvUnit>();

        for(var key : bk){

            var any = i.getInstance(key);

            if(!ServiceUnits.isServiceUnit(any)){
                continue;
            }

            var name = ServiceUnits.getName(any);
            ensureNameValid(name);

            log.info("Registration [ServiceUnit] {}",name);

            try {

                var definition = ServiceUnits.getSrvDefineFromAny(any);
                ActivatedSrvUnit p = new ActivatedSrvUnit();
                p.setSrvUnitName(name);
                p.setSrvUnit(any);
                p.setClassType(any.getClass().getName());
                p.setSrvDefines(definition);
                preparingSubmit.put(name,p);

            } catch (ServiceDefinitionException e) {
                throw new ServiceUnitRegException(e);
            }

        }
        install(preparingSubmit.values().stream().toList());

        var hooks = new ArrayList<Method>();

        //执行服务单元的OnActivatedHook
        for(var v : preparingSubmit.values()){
            var any = v.getSrvUnit();
            RefTools.getNoArgsMethod(any, OnActivated.class,hooks);
            RefTools.executeNoArgsMethodsIgnoreException(any,hooks);
            hooks.clear();
        }

        procMap.putAll(preparingSubmit);
    }

    /**
     * 安装服务单元请求处理器&事件处理器
     * @param preparingSubmit 待提交的服务单元
     */
    private void install(List<ActivatedSrvUnit> preparingSubmit){
        for(var unit : preparingSubmit){

            var name = unit.getSrvUnitName();
            var classType = unit.getClassType();

            for(var def : unit.getSrvDefines()){

                //安装请求处理器
                if(def.getDefType().equals(SrvDefType.REQ_HANDLER)){

                    var vo = requestHandlerMapper.getByPatternAndParamsCount(def.getPattern(),def.getParamCount());

                    //数据库已经注册过rHandler
                    if(vo!=null){
                        //数据库的请求处理器类型与当前服务单元类型不一致
                        if(!vo.getSrvUnitClassType().equals(classType)){
                            requestHandlerMapper.deleteById(vo.getId());
                            log.info("Remove [RequestHandler] {}:{}",vo.getSrvUnitName(),vo.getSrvUnitClassType());
                        }else {
                            log.info("Activation [RequestHandler] {}:{}({})",name,vo.getPattern(),vo.getParamsCount());
                        }
                    }
                    if(vo == null){
                        RequestHandlerVo insert = new RequestHandlerVo();
                        insert.setId(snowflake.nextId());
                        insert.setPattern(def.getPattern());
                        insert.setParams(JSON.toJSONString(def.getParams()));
                        insert.setParamsCount(def.getParamCount());
                        insert.setSrvUnitName(name);
                        insert.setSrvUnitClassType(classType);
                        insert.setStatus(0);
                        insert.setMetadata("");
                        insert.setCreateTime(new Date());
                        requestHandlerMapper.insert(insert);
                        log.info("Install [RequestHandler] {}:{}({})",name,def.getPattern(),def.getParamCount());
                    }

                }

                //安装事件处理器
                if(def.getDefType().equals(SrvDefType.EVENT_HANDLER)){

                    //不安装进程内事件
                    if(! def.isGlobalEventHandler()){
                        continue;
                    }

                    log.info("install [EventHandler] {}:{}({})",name,def.getMethod().getName(),def.getEventName());
                    eventSchedule.register(def);
                }

            }


        }
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
            Application.getInjector().injectMembers(proc);

        } catch (ServiceDefinitionException e) {
            e.printStackTrace();
            log.warn("无法注册服务单元:{} - {} 因为服务单元已损坏.",procName,classType);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 向服务单元转发请求
     */
    public Process forward(ShellRequest request){

        try {
            statementResolver.resolve(request);
        } catch (StatementParsingException e) {
            var cable = request.getShell().getCable().dye(RedDye.pickUp);
            cable.nextLine();
            cable.print("无法解析Statement");
            cable.w(e.toString()).nextLine();
            cable.flush();
            return null;
        }

        //查找数据库中的请求处理器
        var requestHandlerVo = requestHandlerMapper.getByPatternAndParamsCount(request.getPattern(), request.getParams().size());

        if(requestHandlerVo == null){
            log.warn("无法处理用户请求,因为数据库中没有所需的请求处理器 {}.",request.getPattern());
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
        t.setTaskManager(taskManager);
        t.setBackground(procDef.isBackgroundRequestHandler());

        //执行Define
        taskManager.commit(t);
        return t;
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

    public static ActivatedSrvUnit getServiceUnit(String procName){
        return procMap.get(procName);
    }


    private void ensureNameValid(String name) throws ServiceUnitRegException{

        if(StringUtils.isBlank(name)){
            throw new ServiceUnitRegException("the ServiceUnit name cannot be null or blank");
        }

        if(procMap.containsKey(name)){
            throw new ServiceUnitRegException("repeat ServiceUnit name:"+name);
        }

    }



}
