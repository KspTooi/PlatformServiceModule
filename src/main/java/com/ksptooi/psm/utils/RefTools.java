package com.ksptooi.psm.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefTools {


    public static List<?> getParameterType(Method method,int seq){

        var ret = new ArrayList<>();

        var genericParameterTypes = method.getGenericParameterTypes();

        if(genericParameterTypes.length < seq){
            return ret;
        }

        var pType = (ParameterizedType)genericParameterTypes[seq];

        var actualTypeArguments = pType.getActualTypeArguments();

        for(var actual : actualTypeArguments){
            ret.add(actual);
        }

        return ret;
    }


    public static void executeNoArgsMethodsIgnoreException(Object ctx, List<Method> methods){

        for (var m : methods){

            if(m.getParameterCount() != 0){
                continue;
            }

            try {
                m.invoke(ctx);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static <A extends Annotation> List<Method> getNoArgsMethod(Object any,Class<A> anno,List<Method> ret){

        var method = getMethodByAnnotation(any.getClass(), anno);

        for(var m : method){
            if(m.getParameterCount() == 0){
                ret.add(m);
            }
        }

        return ret;
    }


    public static <A extends Annotation> A getAnnotation(Object any,Class<A> anno){

        if(any == null){
            return null;
        }

        return any.getClass().getAnnotation(anno);
    }


    /**
     * 查找类上带有指定注解与value的方法
     * @param clazz 需要查找的类
     * @param inAnno 需要查找的注解
     * @return 失败返回0长度数组
     */
    public static Method[] getMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> inAnno){

        if(clazz == null || inAnno == null){
            return new Method[0];
        }

        Method[] methods = clazz.getMethods();

        List<Method> retMethod = new ArrayList<>();

        for(Method item:methods){

            Annotation annotation = item.getAnnotation(inAnno);

            if(annotation!=null){
                retMethod.add(item);
            }

        }

        return retMethod.toArray(new Method[0]);
    }

}
