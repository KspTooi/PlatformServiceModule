package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.ProcDefine;
import com.ksptooi.uac.commons.ReflectUtils;
import com.ksptooi.uac.core.annatatiotion.Param;
import org.apache.commons.lang3.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ProcTools {

    /**
     * 查找该处理器中的映射
     */
    public static List<ProcDefine> getProcDefine(Class<?> proc) throws ProcDefineException {

        //获取处理器名称
        RequestProcessor annoProc = proc.getAnnotation(RequestProcessor.class);

        if(annoProc == null){
            throw new ProcDefineException("处理器已损坏,原因:处理器需要@RequestProcessor注解. 位于:"+ proc.getName());
        }

        if(!chkProcName(annoProc.value())){
            throw new ProcDefineException("处理器已损坏,原因:处理器名称不合法. 位于:"+ proc.getName());
        }

        final String procName = annoProc.value();

        //获取处理器中的钩子注解
        final Method[] annoOnActivated = ReflectUtils.getMethodByAnnotation(proc, OnActivated.class);
        final Method[] annoOnDestroy = ReflectUtils.getMethodByAnnotation(proc, OnDestroy.class);

        if(annoOnActivated.length > 1 || annoOnDestroy.length > 1){
            throw new ProcDefineException("处理器不支持同时拥有多个相同的钩子注解 位于:"+procName);
        }

        //获取处理器中的请求映射
        Method[] annoReqHandler = ReflectUtils.getMethodByAnnotation(proc, RequestHandler.class);

        List<ProcDefine> ret = new ArrayList<>();

        if(annoOnActivated.length > 0){
            ProcDefine defHook = new ProcDefine();
            defHook.setDefType(ProcDefType.HOOK_ACTIVATED);
            defHook.setMethod(annoOnActivated[0]);
            ret.add(defHook);
        }
        if(annoOnDestroy.length > 0){
            ProcDefine defHook = new ProcDefine();
            defHook.setDefType(ProcDefType.HOOK_DESTROY);
            defHook.setMethod(annoOnDestroy[0]);
            ret.add(defHook);
        }

        if(!chkReqHandlerPattern(annoReqHandler)){
            throw new ProcDefineException("处理器已损坏 原因:至少有一个请求映射Pattern不合法 位于:"+procName);
        }

        if(!chkRepeatWildcard(annoReqHandler)){
            throw new ProcDefineException("处理器已损坏 原因:处理器不支持多个模式为通配符(*)的请求映射 位于:"+procName);
        }

        for(Method m : annoReqHandler){
            final String pattern = m.getAnnotation(RequestHandler.class).value();
            final List<String> alias = getAliasByAnnotation(m);
            final List<String> params = getParamNameByAnnotation(m);
            ProcDefine def = new ProcDefine();
            def.setDefType(ProcDefType.REQ_HANDLER);
            def.setPattern(pattern);
            def.setProcName(procName);
            def.setAlias(alias);
            def.setParams(params);
            def.setParamCount(params.size());
            def.setMethod(m);
            ret.add(def);
        }

        //检查是否有两个一样的请求映射
        if(!checkRepeatMapping(ret)){
            throw new ProcDefineException("处理器已损坏 原因:处理器中有两个相同的映射 位于:"+procName);
        }

        return ret;
    }

    public static String getProcName(Class<?> proc){

        //获取处理器名称
        RequestProcessor annoProc = proc.getAnnotation(RequestProcessor.class);

        if(annoProc == null){
            return null;
        }

        return annoProc.value();
    }

    /**
     * 查找该处理器中的请求映射(定义)
     */
    public static List<ProcDefine> getRequestDefine(Class<?> proc){

        //获取处理器名称
        RequestProcessor annotation = proc.getAnnotation(RequestProcessor.class);

        if(annotation == null){
            return new ArrayList<>();
        }

        //拿到该Class里面带注解的方法列表
        Method[] methodByAnnotation = ReflectUtils.getMethodByAnnotation(proc, RequestName.class);

        if(methodByAnnotation.length < 1){
            return new ArrayList<>();
        }

        List<ProcDefine> ret = new ArrayList<>();

        for (Method method: methodByAnnotation){

            ProcDefine define = new ProcDefine();
            define.setPattern(null);
            define.setProcName(annotation.value());
            define.setAlias(new ArrayList<>());
            define.setParams(new ArrayList<>());
            define.setParamCount(0);
            define.setMethod(method);

            //拿到方法上RequestName注解的值
            String name = method.getAnnotation(RequestName.class).value();
            define.setPattern(name);


            Alias annoAlias = method.getAnnotation(Alias.class);

            //请求有Alias
            if(annoAlias != null && annoAlias.value().length > 0){
                define.setAlias(Arrays.stream(annoAlias.value()).toList());
            }

            //获取请求参数
            List<String> parameter = getParamNameByAnnotation(method);

            if(!parameter.isEmpty()){
                define.setParams(parameter);
                define.setParamCount(parameter.size());
            }

            ret.add(define);
        }

        return ret;
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

    public static boolean chkProcName(String name){

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

    public static boolean checkRepeatMapping(List<ProcDefine> defines){

        Set<String> hSet = new HashSet<>();

        for(ProcDefine def : defines){

            //只判断请求映射器类型
            if(! def.getDefType().equals(ProcDefType.REQ_HANDLER)){
                continue;
            }

            final String identity = def.getProcName() + "::" + def.getPattern() + "::" + def.getParamCount();

            if(hSet.contains(identity)){
                return false;
            }

            hSet.add(identity);
        }

        return true;
    }



}
