package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.SrvDefine;
import com.ksptooi.psm.processor.event.*;
import com.ksptooi.psm.processor.hook.OnActivated;
import com.ksptooi.psm.processor.hook.OnDestroy;
import com.ksptooi.uac.commons.ReflectUtils;
import com.ksptooi.uac.core.annatatiotion.Param;
import org.apache.commons.lang3.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class SrvUnitTools {

    public static final List<String> eventDefine = new ArrayList<>();

    static {
        eventDefine.add(BadRequestEvent.class.getName());
        eventDefine.add(InstallProcHandlerEvent.class.getName());
        eventDefine.add(ProcRegisterEvent.class.getName());
        eventDefine.add(RequestForwardEvent.class.getName());
        eventDefine.add(ShellInputEvent.class.getName());
        eventDefine.add(StatementCommitEvent.class.getName());
        eventDefine.add(UserTypingEvent.class.getName());
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
    public static List<SrvDefine> getSrvDefine(Class<?> srvUnit) throws SrvDefineException {

        //获取服务单元名称
        var annoSrvUnit = srvUnit.getAnnotation(ServiceUnit.class);

        if(annoSrvUnit == null){
            throw new SrvDefineException("服务单元已损坏,原因:服务单元需要@ServiceUnit注解. 位于:"+ srvUnit.getName());
        }

        if(!chkSrvUnitName(annoSrvUnit.value())){
            throw new SrvDefineException("服务单元已损坏,原因:处理器名称不合法. 位于:"+ srvUnit.getName());
        }

        final String procName = annoSrvUnit.value();

        //获取处理器中的钩子注解
        final Method[] annoOnActivated = ReflectUtils.getMethodByAnnotation(srvUnit, OnActivated.class);
        final Method[] annoOnDestroy = ReflectUtils.getMethodByAnnotation(srvUnit, OnDestroy.class);

        if(annoOnActivated.length > 1 || annoOnDestroy.length > 1){
            throw new SrvDefineException("处理器不支持同时拥有多个相同的钩子注解 位于:"+procName);
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
            defHook.setDefType(SrvDefType.HOOK_DESTROY);
            defHook.setMethod(annoOnDestroy[0]);
            ret.add(defHook);
        }

        if(!chkReqHandlerPattern(annoReqHandler)){
            throw new SrvDefineException("处理器已损坏 原因:至少有一个请求映射Pattern不合法 位于:"+procName);
        }

        if(!chkRepeatWildcard(annoReqHandler)){
            throw new SrvDefineException("处理器已损坏 原因:处理器不支持多个模式为通配符(*)的请求映射 位于:"+procName);
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
            throw new SrvDefineException("处理器已损坏 原因:处理器中有两个相同的映射 位于:"+procName);
        }

        //获取处理器中的事件处理器
        ret.addAll(getEventHandlerInSrvUnit(srvUnit));
        return ret;
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

    public static boolean chkSrvUnitName(String name){

        if(StringUtils.isBlank(name)){
            return false;
        }

        if(name.contains("*")){
            return false;
        }

        return true;
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
    public static List<SrvDefine> getEventHandlerInSrvUnit(Class<?> srvUnit) throws SrvDefineException {

        //获取处理器名称
        final String srvUnitName = srvUnit.getAnnotation(ServiceUnit.class).value();

        final Method[] annoEventHandler = ReflectUtils.getMethodByAnnotation(srvUnit, RequestHandler.EventHandler.class);

        List<SrvDefine> ret = new ArrayList<>();

        for(Method m : annoEventHandler){

            //事件处理器要处理的事件类型
            final String eventHandlerType = getEventHandlerType(m);

            if(eventHandlerType == null){
                throw new SrvDefineException("事件处理器已损坏. ProcName:"+srvUnitName + " FuncName:"+m.getName());
            }

            SrvDefine def = new SrvDefine();
            def.setDefType(SrvDefType.EVENT_HANDLER);
            def.setSrvUnitName(srvUnitName);
            def.setMethod(m);
            def.setEventHandlerOrder(m.getAnnotation(RequestHandler.EventHandler.class).order());
            def.setEventHandlerType(eventHandlerType);

            String eventHandlerEventName = getEventHandlerEventName(m);
            def.setEventName(eventHandlerEventName);
            def.setGlobalEventHandler(m.getAnnotation(RequestHandler.EventHandler.class).global());
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


    public static void main(String[] args) throws SrvDefineException {

        List<SrvDefine> srvDefine = SrvUnitTools.getSrvDefine(TestServiceUnit.class);

        System.out.println(srvDefine);

    }



}
