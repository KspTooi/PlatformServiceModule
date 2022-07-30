package com.ksptooi.wphub.modules;

import com.google.inject.AbstractModule;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * To use this helper module, call install(new ComponentScanModule("com.foo", Named.class); in the configure method of
 * another module class.
 */
public final class ComponentScanModule extends AbstractModule {

    private final String packageName;
    private final Set<Class<? extends Annotation>> bindingAnnotations;

    @SafeVarargs
    public ComponentScanModule(String packageName, final Class<? extends Annotation>... bindingAnnotations) {
        this.packageName = packageName;
        this.bindingAnnotations = new HashSet<>(Arrays.asList(bindingAnnotations));
    }

    @Override
    public void configure() {

        Reflections packageReflections = new Reflections(packageName);


        Set<Class<?>> typesAnnotatedWith = packageReflections.getTypesAnnotatedWith(bindingAnnotations.iterator().next());

        Class<?> next = typesAnnotatedWith.iterator().next();



        bindingAnnotations.stream()
                .map(packageReflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .forEach(this::bind);

    }
}
