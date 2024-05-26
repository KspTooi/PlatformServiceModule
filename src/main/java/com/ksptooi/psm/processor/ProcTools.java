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

            //拿到方法上RequestName注解的值
            String name = method.getAnnotation(RequestName.class).value();
            define.setName(name);

            String[] alias = method.getAnnotation(Alias.class).value();

            //请求有Alias
            if(alias != null && alias.length > 0){
                define.setAlias(Arrays.stream(alias).toList());
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



}
