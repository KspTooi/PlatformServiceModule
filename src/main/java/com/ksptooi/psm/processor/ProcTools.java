package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.RequestDefine;
import com.ksptooi.uac.commons.ReflectUtils;
import com.ksptooi.uac.core.annatatiotion.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcTools {


    /**
     * 查找该处理器中的请求映射(定义)
     */
    public static List<RequestDefine> getRequestDefine(Class<?> proc){

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

        List<RequestDefine> ret = new ArrayList<>();

        for (Method method: methodByAnnotation){

            RequestDefine define = new RequestDefine();
            define.setName(null);
            define.setProcName(annotation.value());
            define.setAlias(new ArrayList<>());
            define.setParameters(new ArrayList<>());
            define.setParameterCount(0);
            define.setMethod(method);

            //拿到方法上RequestName注解的值
            String name = method.getAnnotation(RequestName.class).value();
            define.setName(name);


            Alias annoAlias = method.getAnnotation(Alias.class);

            //请求有Alias
            if(annoAlias != null && annoAlias.value().length > 0){
                define.setAlias(Arrays.stream(annoAlias.value()).toList());
            }

            //获取请求参数
            String[] parameter = getParamNameByAnnotation(method);

            if(parameter.length > 0){
                define.setParameters(Arrays.stream(parameter).toList());
                define.setParameterCount(parameter.length);
            }

            ret.add(define);
        }

        return ret;
    }

    /**
     * 获取该方法上的所有全部注解参数名
     */
    private static String[] getParamNameByAnnotation(Method method){

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        List<String> retString = new ArrayList<>();

        for (Annotation[] anno : parameterAnnotations){

            for(Annotation item:anno){

                if(item instanceof Param){
                    retString.add(((Param) item).value());
                }

            }

        }

        return retString.toArray(new String[0]);
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



}
