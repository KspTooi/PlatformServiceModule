package com.ksptooi.guice.compomentscan;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.guice.annotations.MultiInstance;
import com.ksptooi.guice.annotations.Unit;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * To use this helper module, call install(new ComponentScanModule("com.foo", Named.class); in the configure method of
 * another module class.
 */
public final class ComponentScanModule extends AbstractModule {

    private final String packageName;
    private final Set<Class<? extends Annotation>> bindingAnnotations;

    public ComponentScanModule(String packageName){
        this.packageName = packageName;
        this.bindingAnnotations = new HashSet<>();
        this.bindingAnnotations.add(Unit.class);
    }

    @SafeVarargs
    public ComponentScanModule(String packageName, final Class<? extends Annotation>... bindingAnnotations) {
        this.packageName = packageName;
        this.bindingAnnotations = new HashSet<>(Arrays.asList(bindingAnnotations));
    }

    @Override
    public void configure() {

        Reflections packageReflections = new Reflections(packageName);

        //扫描包含指定注解的类
        List<Class<?>> collect = bindingAnnotations.stream()
                .map(packageReflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .collect(Collectors.toList());

        for(Class<?> clazz : collect){

            MultiInstance multi = clazz.getAnnotation(MultiInstance.class);

            if(multi == null){
                //绑定为单例
                bind(clazz).in(Scopes.SINGLETON);
                continue;
            }

            //绑定为多例
            bind(clazz).in(Scopes.NO_SCOPE);
        }


    }
}
