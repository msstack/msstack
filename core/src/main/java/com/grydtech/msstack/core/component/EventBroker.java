package com.grydtech.msstack.core.component;

import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.Event;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.handler.EventHandler;

import java.util.*;
import java.util.logging.Logger;

/**
 * Base class for plugging in BasicEvent Brokers.
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class EventBroker implements AbstractBroker<EventHandler> {

    @AutoInjected
    private static EventBroker instance;
    private static final Logger LOGGER = Logger.getLogger(EventBroker.class.toGenericString());

    private final List<String> streams;
    private final Map<String, Class<? extends EventHandler>> handlers;

    protected EventBroker() {
        streams = new ArrayList<>();
        handlers = new HashMap<>();
    }

    public static EventBroker getInstance() {
        return instance;
    }

    @Override
    public final void subscribe(Class<? extends EventHandler> handlerClass) {
        Event event = handlerClass.getAnnotation(Event.class);

        if (streams.stream().noneMatch(s -> s.equals(event.stream()))) {
            streams.add(event.stream());
        }

        String key = event.stream() + "::" + handlerClass.getSimpleName();
        this.handlers.put(key, handlerClass);
        LOGGER.info(String.format("Added class for subscription: %s", handlerClass.getSimpleName()));
    }

    @Override
    public final void subscribeAll(Set<Class<? extends EventHandler>> subscriberSet) {
        subscriberSet.forEach(this::subscribe);
    }

    @Override
    public final void unsubscribe(Class<? extends EventHandler> handlerClass) {
        LOGGER.info(String.format("Removed class from subscription: %s", handlerClass.getSimpleName()));
    }

    protected final List<String> getStreams() {
        return streams;
    }

    protected final Map<String, Class<? extends EventHandler>> getHandlers() {
        return handlers;
    }

    /**
     * Accepts an BasicEvent and notifies the subscribers
     *
     * @param event Published BasicEvent
     */
    public abstract void publish(BasicEvent event);

    /**
     * Flushes any buffered events and block until completion
     */
    public abstract void flush();
}
