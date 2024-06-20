package com.ksptooi;

import com.google.inject.Injector;

public class Platform {

    public static Injector context(){
        return Application.getInjector();
    }

    public static <T> T getUnit(Class<T> t){
        return Application.getInjector().getInstance(t);
    }

    public static <T> boolean hasUnit(Class<T> t){
        return Application.getInjector().getInstance(t) != null;
    }

}
