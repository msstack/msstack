package com.grydtech.msstack.common.util;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

	private final Reflections reflections;

	public ClassPathScanner(String packagePath) {
		reflections = new Reflections(packagePath);
	}

	public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> baseType) {
		return reflections.getSubTypesOf(baseType);
	}

	public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}
}
