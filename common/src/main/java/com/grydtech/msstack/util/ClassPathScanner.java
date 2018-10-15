package com.grydtech.msstack.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClassPathScanner {

    private final Reflections reflections;

    public ClassPathScanner(String packagePath) {
        reflections = new Reflections(packagePath);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> baseType) {
        return reflections.getSubTypesOf(baseType).stream()
                .filter(this::isConcrete)
                .collect(Collectors.toSet());
    }

    private boolean isConcrete(Class c) {
        final int cm = c.getModifiers();
        return !Modifier.isInterface(cm) && !Modifier.isAbstract(cm) && !Modifier.isNative(cm);
    }

    public <T extends Annotation> Set<Class<?>> getTypesAnnotatedWith(Class<T> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    public <T> T getInstance(Class<T> tClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return getSubTypesOf(tClass)
                .stream()
                .filter(this::isConcrete)
                .findFirst()
                .orElseThrow(ClassNotFoundException::new)
                .newInstance();
    }
}
