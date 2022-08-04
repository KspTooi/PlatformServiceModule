package com.ksptooi.asf.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflectUtils {

    //查找类上带有指定注解与value的方法
    public static Method[] getMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> inAnno){

        Method[] methods = clazz.getMethods();

        Method[] retMethod = new Method[0];

        for(Method item:methods){

            Annotation annotation = item.getAnnotation(inAnno);

            if(annotation!=null){
                retMethod = ArrayUtils.append(retMethod,item);
            }

        }

        return retMethod;
    }

}
