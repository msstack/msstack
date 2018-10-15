package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.configuration.ApplicationConfiguration;

public abstract class KafkaService {
    protected final ApplicationConfiguration applicationConfiguration;

    public KafkaService(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public abstract void start();
}
