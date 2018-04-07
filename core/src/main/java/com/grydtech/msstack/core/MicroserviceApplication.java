package com.grydtech.msstack.core;

import com.grydtech.msstack.common.util.ClassPathScanner;
import com.grydtech.msstack.core.annotations.Handler;
import com.grydtech.msstack.core.annotations.Microservice;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public abstract class MicroserviceApplication extends Application {

	public Set<Class<? extends GenericHandler>> getHandlers() {
		ClassPathScanner classPathScanner = new ClassPathScanner(getClass().getPackage().getName());
		Set<Class<? extends GenericHandler>> handlers = new HashSet<>();
		classPathScanner.getTypesAnnotatedWith(Handler.class).forEach(c -> {
			if (GenericHandler.class.isAssignableFrom(c)) handlers.add(c.asSubclass(GenericHandler.class));
		});

		return handlers;
	}

	protected int getPort() {
		return Integer.parseInt(getClass().getAnnotation(Microservice.class).port());
	}

	protected String getHost() {
		return getClass().getAnnotation(Microservice.class).host();
	}

	public abstract void start() throws Exception;
}
