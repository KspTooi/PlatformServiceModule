package com.ksptooi.psm.utils;

import com.ksptooi.psm.processor.ServiceUnit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RefTools {




    public static <A extends Annotation> A getAnnotation(Object any,Class<A> anno){

        if(any == null){
            return null;
        }

        return any.getClass().getAnnotation(anno);
    }




    //查找类上带有指定注解与value的方法
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
