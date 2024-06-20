package com.ksptooi.psm.processor;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.ksptooi.psm.processor.entity.ActivatedSrvUnit;
import com.ksptooi.psm.processor.entity.SrvDefine;
import com.ksptooi.psm.processor.event.*;
import com.ksptooi.psm.subsystem.Module;
import com.ksptooi.psm.subsystem.SubSystemEntry;
import com.ksptooi.psm.utils.RefTools;
import com.ksptooi.uac.commons.ReflectUtils;
import com.ksptooi.uac.core.annatatiotion.Param;
import org.apache.commons.lang3.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ServiceUnits {

    public static final List<String> eventDefine = new ArrayList<>();

    static {
        eventDefine.add(BadRequestEvent.class.getName());
        eventDefine.add(InstallProcHandlerEvent.class.getName());
        eventDefine.add(ServiceUnitRegisterEvent.class.getName());
        eventDefine.add(RequestForwardEvent.class.getName());
        eventDefine.add(ShellInputEvent.class.getName());
        eventDefine.add(StatementCommitEvent.class.getName());
        eventDefine.add(UserTypingEvent.class.getName());
    }


    public static void findModules(Object any, List<Method> ret){

        if(!isServiceUnit(any) || !isEntry(any)){
            return;
        }

        var method = RefTools.getMethodByAnnotation(any.getClass(), Module.class);

        for(var m : method){

            if(m.getParameterCount() > 0){
                continue;
            }

            ret.add(m);
        }

    }



    /**
     * 判断一个类是否为服务单元
     */
    public static boolean isServiceUnit(Object any){
        return any.getClass().getAnnotation(ServiceUnit.class) != null;
    }

    public static boolean isEntry(Object any){
        return any.getClass().getAnnotation(SubSystemEntry.class) != null;
    }


    public static List<SrvDefine> getSrvDefineFromAny(Object any) throws ServiceDefinitionException {

        var ret = new ArrayList<SrvDefine>();
        var annoSrvUnit = RefTools.getAnnotation(any, ServiceUnit.class);

        if(annoSrvUnit == null){
            return ret;
        }

        var clazz = any.getClass();
        var srvUnitName = annoSrvUnit.value();

        ensureServiceUnitNameValid(srvUnitName);

        //获取SrvUnit中的Hook
        var hookActivated = RefTools.getMethodByAnnotation(clazz, OnActivated.class);
        var hookDestroyed = RefTools.getMethodByAnnotation(clazz, OnDestroyed.class);

        if(hookActivated.length > 0){
            SrvDefine defHook = new SrvDefine();
            defHook.setDefType(SrvDefType.HOOK_ACTIVATED);
            defHook.setMethod(hookActivated[0]);
            ret.add(defHook);
        }

        if(hookDestroyed.length > 0){
            SrvDefine defHook = new SrvDefine();
            defHook.setDefType(SrvDefType.HOOK_DESTROYED);
            defHook.setMethod(hookActivated[0]);
            ret.add(defHook);
        }

        //获取SrvUnit中的RequestHandler
        var requestHandlers = RefTools.getMethodByAnnotation(clazz, RequestHandler.class);

        ensureCorrectRequestHandlers(requestHandlers);

        for(var rHandler : requestHandlers){

            var background = rHandler.getAnnotation(Background.class);
            var pattern = rHandler.getAnnotation(RequestHandler.class).value();
            var alias = getAliasByAnnotation(rHandler);
            var params = getParamNameByAnnotation(rHandler);

            SrvDefine def = new SrvDefine();
            def.setDefType(SrvDefType.REQ_HANDLER);
            def.setPattern(pattern);
            def.setSrvUnitName(srvUnitName);
            def.setAlias(alias);
            def.setParams(params);
            def.setParamCount(params.size());
            def.setMethod(rHandler);

            if(background!=null){
                def.setBackgroundRequestHandler(true);
            }else {
                def.setBackgroundRequestHandler(false);
            }

            ret.add(def);
        }

        //获取SrvUnit中的EventHandler
        var eventHandlers = RefTools.getMethodByAnnotation(clazz,EventHandler.class);

        for(var eHandler : eventHandlers){

            var type = getEventHandlerType(eHandler);

            if(type == null){
                throw new ServiceDefinitionException("事件处理器已损坏. ServiceUnitName:"+srvUnitName + " FuncName:"+eHandler.getName());
            }

            SrvDefine def = new SrvDefine();
            def.setDefType(SrvDefType.EVENT_HANDLER);
            def.setSrvUnitName(srvUnitName);
            def.setMethod(eHandler);
            def.setEventHandlerOrder(eHandler.getAnnotation(EventHandler.class).order());
            def.setEventHandlerType(type);

            var eventName = getEventHandlerEventName(eHandler);
            def.setEventName(eventName);
            def.setGlobalEventHandler(eHandler.getAnnotation(EventHandler.class).global());
            ret.add(def);
        }

        return ret;
    }



    private static void ensureCorrectRequestHandlers(Method[] handlers) throws ServiceDefinitionException{

        var hasWildcard = false;
        var set = new HashSet<String>();

        for(var m : handlers){

            var anno = m.getAnnotation(RequestHandler.class);
            var pattern = anno.value();

            //Pattern合法性检测
            if(StringUtils.isBlank(pattern)){
                throw new ServiceDefinitionException("pattern is blank:"+pattern);
            }

            //不能以空格开头&空格结尾
            if(!pattern.equals(pattern.trim())){
                throw new ServiceDefinitionException("pattern:" + pattern + "cannot start or end with a space");
            }

            //包含通配符
            if(pattern.contains("*") && pattern.length() != 1){
                throw new ServiceDefinitionException("pattern:" + pattern + "cannot contain \"*\" unless it is the only character");
            }

            //Pattern重复检测
            if(!pattern.equals("*")){
                if(set.contains(pattern)){
                    throw new ServiceDefinitionException("repeated pattern:"+pattern);
                }
                set.add(pattern);
            }

            //同一服务单元中不能出现多个通配符映射
            if(pattern.equals("*")){
                if(hasWildcard){
                    throw new ServiceDefinitionException("repeated wildcard pattern in same service unit");
                }
                hasWildcard = true;
            }

        }
    }


    /**
     * 根据类模板创建服务单元实例
     */
    public static List<Object> getSrvUnitInstance(Set<Class<?>> classSet){

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
     * 查找该服务单元中的映射
     */
    public static List<SrvDefine> getSrvUnits(Class<?> srvUnit) throws ServiceDefinitionException {

        //获取服务单元名称
        var annoSrvUnit = srvUnit.getAnnotation(ServiceUnit.class);

        if(annoSrvUnit == null){
            throw new ServiceDefinitionException("服务单元已损坏,原因:服务单元需要@ServiceUnit注解. 位于:"+ srvUnit.getName());
        }

        ensureServiceUnitNameValid(annoSrvUnit.value());

        final String procName = annoSrvUnit.value();

        //获取处理器中的钩子注解
        final Method[] annoOnActivated = ReflectUtils.getMethodByAnnotation(srvUnit, OnActivated.class);
        final Method[] annoOnDestroy = ReflectUtils.getMethodByAnnotation(srvUnit, OnDestroyed.class);

        if(annoOnActivated.length > 1 || annoOnDestroy.length > 1){
            throw new ServiceDefinitionException("处理器不支持同时拥有多个相同的钩子注解 位于:"+procName);
        }

        //获取处理器中的请求映射
        Method[] annoReqHandler = ReflectUtils.getMethodByAnnotation(srvUnit, RequestHandler.class);

        List<SrvDefine> ret = new ArrayList<>();

        if(annoOnActivated.length > 0){
            SrvDefine defHook = new SrvDefine();
            defHook.setDefType(SrvDefType.HOOK_ACTIVATED);
            defHook.setMethod(annoOnActivated[0]);
            ret.add(defHook);
        }
        if(annoOnDestroy.length > 0){
            SrvDefine defHook = new SrvDefine();
            defHook.setDefType(SrvDefType.HOOK_DESTROYED);
            defHook.setMethod(annoOnDestroy[0]);
            ret.add(defHook);
        }

        if(!chkReqHandlerPattern(annoReqHandler)){
            throw new ServiceDefinitionException("处理器已损坏 原因:至少有一个请求映射Pattern不合法 位于:"+procName);
        }

        if(!chkRepeatWildcard(annoReqHandler)){
            throw new ServiceDefinitionException("处理器已损坏 原因:处理器不支持多个模式为通配符(*)的请求映射 位于:"+procName);
        }

        for(Method m : annoReqHandler){
            final String pattern = m.getAnnotation(RequestHandler.class).value();
            final List<String> alias = getAliasByAnnotation(m);
            final List<String> params = getParamNameByAnnotation(m);
            SrvDefine def = new SrvDefine();
            def.setDefType(SrvDefType.REQ_HANDLER);
            def.setPattern(pattern);
            def.setSrvUnitName(procName);
            def.setAlias(alias);
            def.setParams(params);
            def.setParamCount(params.size());
            def.setMethod(m);
            ret.add(def);
        }

        //检查是否有两个一样的请求映射
        if(!checkRepeatMapping(ret)){
            throw new ServiceDefinitionException("处理器已损坏 原因:处理器中有两个相同的映射 位于:"+procName);
        }

        //获取处理器中的事件处理器
        ret.addAll(getEventHandlerInSrvUnit(srvUnit));
        return ret;
    }


    public static String getName(Object any){
        return getSrvUnitName(any.getClass());
    }

    public static String getSrvUnitName(Class<?> proc){

        //获取处理器名称
        var annoProc = proc.getAnnotation(ServiceUnit.class);

        if(annoProc == null){
            return null;
        }

        return annoProc.value();
    }

    private static List<String> getAliasByAnnotation(Method m){

        Alias annoAlias = m.getAnnotation(Alias.class);

        if(annoAlias == null || annoAlias.value().length < 1){
            return new ArrayList<>();
        }

        return Arrays.stream(annoAlias.value()).toList();
    }


    /**
     * 获取该方法上的所有全部注解参数名
     */
    private static List<String> getParamNameByAnnotation(Method method){

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        List<String> retString = new ArrayList<>();

        for (Annotation[] anno : parameterAnnotations){

            for(Annotation item:anno){

                if(item instanceof Param){
                    retString.add(((Param) item).value());
                }

            }

        }

        return retString;
    }


    public static Object[] assemblyParams(Method m,List<String> outsideParams,Object... innerParam){
        return assemblyParams(m,innerParam,outsideParams);
    }


    /**
     * 组装方法参数
     * @param method 需要组装参数的方法
     * @param innerParam 需要注入的内部组件
     * @param stringParam 外部入参
     * @return 返回已组装好的参数组
     */
    public static Object[] assemblyParams(Method method, Object[] innerParam, List<String> stringParam){

        if(stringParam==null){
            stringParam = new ArrayList<>();
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        //参数不足
        if((parameterTypes.length - innerParam.length) > stringParam.size()){
            return null;
        }

        Object[] params = new Object[method.getParameterCount()];

        int paramCount = 0;

        for (int i = 0; i < parameterTypes.length; i++) {

            boolean isInnerParam = false;

            //判断当前参数类型是否为innerParam中的类型
            for(Object item : innerParam){
                if(parameterTypes[i].isInstance(item)){
                    params[i] = item;
                    isInnerParam = true;
                }
            }

            try{
                if(!isInnerParam){
                    params[i] = stringParam.get(paramCount);
                    paramCount ++;
                }
            }catch (IndexOutOfBoundsException e){
                return null;
            }

        }

        return params;
    }


    public static void ensureServiceUnitNameValid(String name) throws ServiceDefinitionException {

        if(StringUtils.isBlank(name)){
            throw new ServiceDefinitionException("服务单元已损坏,原因:服务单元名称不合法. 位于:"+ name);
        }

        if(name.contains("*")){
            throw new ServiceDefinitionException("服务单元已损坏,原因:服务单元名称不能包含通配符 位于:"+ name);
        }

    }


    public static boolean chkReqHandlerPattern(Method[] methods){

        for(Method m : methods){

            RequestHandler anno = m.getAnnotation(RequestHandler.class);

            if(anno == null){
                continue;
            }

            final String pattern = anno.value();

            if(StringUtils.isBlank(pattern)){
                return false;
            }

        }

        return true;
    }


    public static boolean chkRepeatWildcard(Method[] m){

        if(m == null || m.length < 1){
            return true;
        }

        int count = 0;

        for(Method item : m){

            RequestHandler anno = item.getAnnotation(RequestHandler.class);

            if(anno != null && anno.value().equals("*")){
                count++;
            }

        }

        return count <= 1;
    }

    public static boolean checkRepeatMapping(List<SrvDefine> defines){

        Set<String> hSet = new HashSet<>();

        for(SrvDefine def : defines){

            //只判断请求映射器类型
            if(! def.getDefType().equals(SrvDefType.REQ_HANDLER)){
                continue;
            }

            final String identity = def.getSrvUnitName() + "::" + def.getPattern() + "::" + def.getParamCount();

            if(hSet.contains(identity)){
                return false;
            }

            hSet.add(identity);
        }

        return true;
    }

    /**
     * 获取处理器中的事件处理器
     */
    public static List<SrvDefine> getEventHandlerInSrvUnit(Class<?> srvUnit) throws ServiceDefinitionException {

        //获取处理器名称
        final String srvUnitName = srvUnit.getAnnotation(ServiceUnit.class).value();

        final Method[] annoEventHandler = ReflectUtils.getMethodByAnnotation(srvUnit, EventHandler.class);

        List<SrvDefine> ret = new ArrayList<>();

        for(Method m : annoEventHandler){

            //事件处理器要处理的事件类型
            final String eventHandlerType = getEventHandlerType(m);

            if(eventHandlerType == null){
                throw new ServiceDefinitionException("事件处理器已损坏. ProcName:"+srvUnitName + " FuncName:"+m.getName());
            }

            SrvDefine def = new SrvDefine();
            def.setDefType(SrvDefType.EVENT_HANDLER);
            def.setSrvUnitName(srvUnitName);
            def.setMethod(m);
            def.setEventHandlerOrder(m.getAnnotation(EventHandler.class).order());
            def.setEventHandlerType(eventHandlerType);

            String eventHandlerEventName = getEventHandlerEventName(m);
            def.setEventName(eventHandlerEventName);
            def.setGlobalEventHandler(m.getAnnotation(EventHandler.class).global());
            ret.add(def);
        }

        return ret;
    }


    public static String getEventHandlerType(Method m){

        //获取第一个形参
        if(m.getParameterCount() < 1){
            return null;
        }

        final Class<?> firstParam = m.getParameterTypes()[0];
        final String firstParamTN = firstParam.getName();

        for(String def : eventDefine){
            if(def.equals(firstParamTN)){
                return firstParamTN;
            }
        }

        return null;
    }
    public static String getEventHandlerEventName(Method m){

        //获取第一个形参
        if(m.getParameterCount() < 1){
            return null;
        }

        final Class<?> firstParam = m.getParameterTypes()[0];
        return firstParam.getSimpleName();
    }


    public static void main(String[] args) throws ServiceDefinitionException {

        List<SrvDefine> srvDefine = ServiceUnits.getSrvUnits(TestServiceUnit.class);

        System.out.println(srvDefine);

    }



}
