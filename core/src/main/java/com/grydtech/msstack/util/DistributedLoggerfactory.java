package com.grydtech.msstack.util;

public final class DistributedLoggerfactory {

    private DistributedLoggerfactory() {
    }

    public static DistributedLogger getLogger(String className) {
        return new DistributedLoggerKafkaAppender(className);
    }
}
