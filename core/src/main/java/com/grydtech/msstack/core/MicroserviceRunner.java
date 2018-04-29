package com.grydtech.msstack.core;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MicroserviceRunner {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceRunner.class.toGenericString());


    private MicroserviceRunner() {
    }

    public static void run(Class<? extends MicroserviceApplication> applicationClass) throws Exception {
        MicroserviceApplication microserviceApplication = applicationClass.newInstance();

        Set<Class<? extends GenericHandler>> handlers = microserviceApplication.getHandlers();

        handlers.forEach(handler -> {
            try {
                if (handler.isAssignableFrom(EventHandler.class)) {
                    MessageBroker.class.newInstance().registerHandler(handler.asSubclass(EventHandler.class));
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);

            }
        });

        microserviceApplication.start();
    }
}
