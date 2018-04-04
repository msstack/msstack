package com.grydtech.msstack.microservices.jaxrs;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class JaxRSApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Reflections baseReflections = new Reflections(JaxRSApplication.class.getPackage().getName());
		Set<Class<? extends Feature>> defaultFeatures = baseReflections.getSubTypesOf(Feature.class);
		Set<Class<?>> classes = new HashSet<>(defaultFeatures);

		String packageName = getClass().getPackage().getName();
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> restHandlers = reflections.getTypesAnnotatedWith(Path.class);
		Set<Class<? extends Feature>> features = reflections.getSubTypesOf(Feature.class);
		classes.addAll(restHandlers);
		classes.addAll(features);

		return classes;
	}
}
