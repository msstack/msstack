package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class EventStoreConfiguration {
    
    private String eventStoreName = null;
    
    private String node = null;
    
    private String port = null;
    
    public String getPort() {
        return port;
    }
    
    public String getNode() {
        return node;
    }
    
    public String getEventStoreName() {
        
        return eventStoreName;
    }
}
