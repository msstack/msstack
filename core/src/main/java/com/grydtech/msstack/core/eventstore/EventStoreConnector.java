package com.grydtech.msstack.core.eventstore;

import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Value;
import com.grydtech.msstack.core.configuration.ApplicationConfiguration;

import java.util.List;
import java.util.UUID;

/**
 * Created by dileka on 9/19/18.
 */
//@FrameworkComponent
public abstract class EventStoreConnector {
    
    @Value
    protected static ApplicationConfiguration applicationConfiguration;
    
    @AutoInjected
    private static EventStoreConnector instance;
    
    protected EventStoreConnector() {
    }
    
    public static EventStoreConnector getInstance() {
        return instance;
    }
    
    public abstract void putEvent(BasicEvent basicEvent);
    
    public abstract BasicEvent getEvent(UUID uuid);
    
    public abstract List<BasicEvent> getEventsByTopic(String topic);
    
}
