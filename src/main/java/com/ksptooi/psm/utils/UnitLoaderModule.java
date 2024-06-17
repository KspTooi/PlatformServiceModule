package com.ksptooi.psm.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.ksptooi.guice.annotations.MultiInstance;
import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.subsystem.SubSystemManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class UnitLoaderModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(UnitLoaderModule.class);

    private final String packageName;
    private final Set<Class<? extends Annotation>> bindingAnnotations;
    private final SubSystemManager subSystemManager;

    public UnitLoaderModule(String packageName){
        this.packageName = packageName;
        this.bindingAnnotations = new HashSet<>();
        this.bindingAnnotations.add(Unit.class);
        subSystemManager = null;
    }

    @SafeVarargs
    public UnitLoaderModule(String packageName, final Class<? extends Annotation>... bindingAnnotations) {
        this.packageName = packageName;
        this.bindingAnnotations = new HashSet<>(Arrays.asList(bindingAnnotations));
        subSystemManager = null;
    }

    public UnitLoaderModule(SubSystemManager ssm){
        packageName = null;
        bindingAnnotations = null;
        subSystemManager = ssm;
    }

    @Override
    public void configure() {

        Reflections packageReflections = new Reflections(packageName);

        //扫描包含指定注解的类
        List<Class<?>> collect = bindingAnnotations.stream()
                .map(packageReflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .toList();

        for(Class<?> clazz : collect){

            MultiInstance multi = clazz.getAnnotation(MultiInstance.class);

            if(multi == null){
                //绑定为单例
                bind(clazz).annotatedWith(Names.named("")).in(Scopes.SINGLETON);
                continue;
            }

            //绑定为多例
            bind(clazz).in(Scopes.NO_SCOPE);
        }


    }
}
