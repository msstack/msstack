package com.grydtech.msstack.config;

public enum ConfigKey {

    // Service
    SERVICE_NAME("config.service.name"),

    // Snapshot (LevelDB)
    SNAPSHOT_NAME("config.snapshot.name"),

    // Event Store (Cassandra)
    EVENTSTORE_NAME("config.eventstore.name"),
    EVENTSTORE_HOST("config.eventstore.host"),
    EVENTSTORE_PORT("config.eventstore.port"),

    // ConsumerMessage Bus (Kafka)
    BUS_RETRIES("config.bus.retries"),
    BUS_INTERVAL("config.bus.interval"),
    BUS_ACKS("config.bus.acks"),
    BUS_DELAY("config.bus.delay"),

    // Configuration Store (kafka)
    CONFIG_BOOTSTRAP("config.bootstrap");

    private String value;

    ConfigKey(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return value;
    }
}
