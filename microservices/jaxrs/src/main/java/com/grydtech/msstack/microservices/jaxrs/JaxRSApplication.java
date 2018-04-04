package com.grydtech.msstack.microservices.jaxrs;

import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.Set;

import com.grydtech.msstack.core.MicroserviceApplication;
import com.grydtech.msstack.microservices.jaxrs.features.JacksonFeature;

public abstract class JaxRSApplication extends MicroserviceApplication {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = getHandlers();
		classes.removeIf(handler -> !handler.isAnnotationPresent(Path.class));
		classes.add(JacksonFeature.class);
		return classes;
	}
}
