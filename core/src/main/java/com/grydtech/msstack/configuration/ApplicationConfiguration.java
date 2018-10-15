package com.grydtech.msstack.configuration;

@SuppressWarnings("all")
public final class ApplicationConfiguration {

    private DatabaseConfiguration database = null;
    private EventStoreConfiguration eventStore = null;
    private MessageBusConfiguration messageBroker = null;
    private ServiceRegistryConfiguration serviceRegistry = null;

    private ApplicationConfiguration() {

    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }

    public EventStoreConfiguration getEventStoreConfiguration() {
        return eventStore;
    }

    public MessageBusConfiguration getMessageBusConfiguration() {
        return messageBroker;
    }

    public ServiceRegistryConfiguration getServiceRegistryConfiguration() {
        return serviceRegistry;
    }
}
