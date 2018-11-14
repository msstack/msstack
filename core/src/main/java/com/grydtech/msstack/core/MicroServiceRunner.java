package com.grydtech.msstack.core;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.util.DIUtils;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with static start method that can be used to start a micro service
 */
public final class MicroServiceRunner {

    private static final Logger LOGGER = Logger.getLogger(MicroServiceRunner.class.getName());

    private MicroServiceRunner() {
    }

    public static void run(Class<? extends Application> appClass, String[] args) throws Exception {
        String serviceId = args.length > 0 ? args[0] : UUID.randomUUID().toString();
        ConfigurationProperties.set(ConfigKey.SERVICE_ID, serviceId);

        DIUtils.resolveAll();
        Application application = appClass.getConstructor().newInstance();
        LOGGER.log(Level.ALL, "Starting application...");
        application.start();
        LOGGER.log(Level.ALL, "Application Started.");
    }
}
