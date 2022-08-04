package com.ksptooi.asf.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReflectUtils {

    //查找类上带有指定注解与value的方法
    public static Method[] getMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> inAnno){

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
