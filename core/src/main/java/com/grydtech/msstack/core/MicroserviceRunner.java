package com.grydtech.msstack.core;

import com.grydtech.msstack.util.DependencyInjectorUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with static run method that can be used to run a microservice
 */
@SuppressWarnings("unused")
public final class MicroserviceRunner {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceRunner.class.toGenericString());

    private MicroserviceRunner() {

    }

    public static void run(Class<? extends MicroserviceApplication> applicationClass) throws Exception {
        // Inject dependencies from classpath
        DependencyInjectorUtils.inject();
        LOGGER.log(Level.ALL, "Starting application...");
        // Start Application
        applicationClass.newInstance().run();
        LOGGER.log(Level.ALL, "MicroserviceApplication Started.");
    }
}
