package com.ksptooi.psm.subsystem;

import com.ksptooi.psm.utils.RefTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class SubSystems {


    public static void executeInstallHooks(Object context,List<Method> ret) throws Exception{

        for(var m : ret){
            Object invoke = m.invoke(context);
        }

    }

    public static void findInstalledHook(Object entry, List<Method> ret){
        if(!isEntry(entry)){
            return;
        }
        getNoArgsMethod(entry.getClass(), OnInstalled.class,ret);
    }

    public static void findUninstallHook(Object entry,List<Method> ret){
        if(!isEntry(entry)){
            return;
        }
        getNoArgsMethod(entry.getClass(),OnUninstall.class,ret);
    }

    public static void findModules(Object entry, List<Method> ret){

        if(!isEntry(entry)){
            return;
        }

        var method = RefTools.getMethodByAnnotation(entry.getClass(), Module.class);

        for(var m : method){

            if(m.getParameterCount() > 0){
                continue;
            }

            ret.add(m);
        }

    }

    public static boolean isEntry(Object any){
        return any.getClass().getAnnotation(SubSystemEntry.class) != null;
    }

    private static <T extends Annotation> void getNoArgsMethod(Class<?> any, Class<T> anno,List<Method> ret){
        var methods = RefTools.getMethodByAnnotation(any, anno);

        for(var m : methods){

            if(m.getParameterCount() > 0){
                continue;
            }

            ret.add(m);
        }
    }

}
