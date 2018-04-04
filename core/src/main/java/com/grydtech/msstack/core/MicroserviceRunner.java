package com.grydtech.msstack.core;

import java.util.Set;

public class MicroserviceRunner {

	public static void run(Class<? extends MicroserviceApplication> applicationClass) throws Exception {
		MicroserviceApplication microserviceApplication = applicationClass.newInstance();

		Set<Class<?>> handlers = microserviceApplication.getHandlers();

		handlers.forEach(handler -> {
			try {
				if (handler.isAssignableFrom(EventHandler.class)) {
					MessageBroker.class.newInstance().registerHandler(handler.asSubclass(EventHandler.class));
				}
			}
			catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

		microserviceApplication.start();
	}
}
