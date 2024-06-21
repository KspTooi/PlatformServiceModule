package com.ksptooi.psm.utils;

import com.ksptooi.psm.processor.ServiceUnit;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RefTools {


    public static void executeNoArgsMethodsNE(Object ctx,List<Method> methods){

        for (var m : methods){
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
