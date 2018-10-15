package com.grydtech.msstack.core.connectors.eventstore;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectConfiguration;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.connectors.IConnector;
import com.grydtech.msstack.core.types.messaging.Event;

import java.util.List;
import java.util.UUID;

/**
 * Created by dileka on 9/19/18.
 */
@FrameworkComponent
public abstract class EventStoreConnector implements IConnector {

    @InjectConfiguration
    protected static ApplicationConfiguration applicationConfiguration;

    @InjectInstance
    private static EventStoreConnector instance;

    protected EventStoreConnector() {
    }

    public static EventStoreConnector getInstance() {
        return instance;
    }

    public abstract void push(Event event);

    public abstract Event getEvent(UUID uuid);

    public abstract List<Event> getEventsByTopic(String topic);
}
