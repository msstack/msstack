package com.grydtech.msstack.config;

public enum ConfigKey {

    // Snapshot (LevelDB)
    SNAPSHOT_NAME("config.snapshot.name"),
    SNAPSHOT_HOST("config.snapshot.host"),
    SNAPSHOT_PORT("config.snapshot.port"),

    // Event Store (Cassandra)
    EVENTSTORE_NAME("config.eventstore.name"),
    EVENTSTORE_HOST("config.eventstore.host"),
    EVENTSTORE_PORT("config.eventstore.port"),

    // API Gateway (Netty)
    GATEWAY_HOST("config.gateway.host"),
    GATEWAY_PORT("config.gateway.port"),

    // Message Bus (Kafka)
    BUS_RETRIES("config.bus.retries"),
    BUS_INTERVAL("config.bus.interval"),
    BUS_ACKS("config.bus.acks"),
    BUS_DELAY("config.bus.delay"),

    // Configuration Store (Zookeeper)
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
