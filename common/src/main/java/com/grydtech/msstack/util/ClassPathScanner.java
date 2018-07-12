package com.grydtech.msstack.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathScanner {

    private final Reflections reflections;

    public ClassPathScanner(String packagePath) {
        reflections = new Reflections(packagePath);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> baseType) {
        return reflections.getSubTypesOf(baseType).stream()
                .filter(aClass -> !Modifier.isAbstract(aClass.getModifiers()))
                .collect(Collectors.toSet());
    }

    public <T extends Annotation> Set<Class<?>> getTypesAnnotatedWith(Class<T> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    public <T> T getInstance(Class<T> tClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return getSubTypesOf(tClass)
                .stream()
                .filter(aClass -> !Modifier.isAbstract(aClass.getModifiers()))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new)
                .newInstance();
    }
}
