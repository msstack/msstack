package com.grydtech.msstack.core.component;

import com.grydtech.msstack.core.Event;
import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.ServerComponent;
import com.grydtech.msstack.core.handler.EventHandler;

/**
 * Base class for plugging in Event Brokers.
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class EventBroker implements AbstractBroker<EventHandler> {

    @AutoInjected
    private static EventBroker instance;

    public static EventBroker getInstance() {
        return instance;
    }

    /**
     * Accepts an Event and notifies the subscribers
     *
     * @param event Published Event
     */
    public abstract void publish(Event event);

    /**
     * Flushes any buffered events and block until completion
     */
    public abstract void flush();

    @Override
    public final int getPort() {
        return getClass().getAnnotation(ServerComponent.class).port();
    }

    @Override
    public final String getHost() {
        return getClass().getAnnotation(ServerComponent.class).host();
    }
}
