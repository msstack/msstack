package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class ApplicationConfiguration {
    private ServerConfiguration server = null;
    private MessageBrokerConfiguration messageBroker = null;
    private ServiceRegistryConfiguration serviceRegistry = null;
    private DatabaseConfiguration database = null;
    private EventStoreConfiguration eventStore = null;
    
    public EventStoreConfiguration getEventStore() {
        return eventStore;
    }
    
    public DatabaseConfiguration getDatabase() {
        return database;
    }
    
    public ServerConfiguration getServer() {
        return server;
    }

    public MessageBrokerConfiguration getMessageBroker() {
        return messageBroker;
    }

    public ServiceRegistryConfiguration getServiceRegistry() {
        return serviceRegistry;
    }
}
