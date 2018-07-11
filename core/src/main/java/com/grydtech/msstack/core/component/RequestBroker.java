package com.grydtech.msstack.core.component;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Server;
import com.grydtech.msstack.core.handler.RequestHandler;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for plugging in a Request Broker.
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class RequestBroker extends Application implements AbstractBroker<RequestHandler> {

    @AutoInjected
    private static RequestBroker instance;

    private Set<Class<? extends RequestHandler>> subscribers;

    protected RequestBroker() {
        subscribers = new HashSet<>();
    }

    public static RequestBroker getInstance() {
        return instance;
    }

    protected Set<Class<? extends RequestHandler>> getHandlerClasses() {
        return subscribers;
    }

    @Override
    public final Set<Class<?>> getClasses() {
        return subscribers.stream().map(aClass -> (Class<?>) aClass).collect(Collectors.toSet());
    }

    @Override
    public final void subscribe(Class<? extends RequestHandler> subscriberClass) {
        this.subscribers.add(subscriberClass);
    }

    @Override
    public final void subscribeAll(Set<Class<? extends RequestHandler>> subscriberSet) {
        this.subscribers.addAll(subscriberSet);
    }

    @Override
    public final void unsubscribe(Class<? extends RequestHandler> subscriberClass) {
        this.subscribers.remove(subscriberClass);
    }

    @Override
    public final int getPort() {
        return getClass().getAnnotation(Server.class).port();
    }

    @Override
    public final String getHost() {
        return getClass().getAnnotation(Server.class).host();
    }
}
