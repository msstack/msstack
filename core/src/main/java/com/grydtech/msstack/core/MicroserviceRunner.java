package com.grydtech.msstack.core;

import com.grydtech.msstack.util.DependencyInjectorUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with static start method that can be used to start a microservice
 */
@SuppressWarnings("unused")
public final class MicroserviceRunner {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceRunner.class.toGenericString());

    private MicroserviceRunner() {
    }

    public static void run(Class<? extends MicroserviceApplication> applicationClass) throws Exception {

        DependencyInjectorUtils.resolveAll();
        MicroserviceApplication microserviceApplication = applicationClass.newInstance();
        LOGGER.log(Level.ALL, "Starting application...");
        microserviceApplication.start();
        LOGGER.log(Level.ALL, "MicroserviceApplication Started.");
        microserviceApplication.wait();
    }
}
