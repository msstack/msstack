package com.grydtech.msstack.core.component;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.ServerComponent;
import com.grydtech.msstack.core.handler.RequestHandler;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for plugging in a Request Broker.
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class RequestBroker extends Application implements AbstractBroker<RequestHandler> {

    @AutoInjected
    private static RequestBroker instance;

    private Set<Class<?>> handlers;

    protected RequestBroker() {
        handlers = new HashSet<>();
    }

    public static RequestBroker getInstance() {
        return instance;
    }

    @Override
    public final Set<Class<?>> getClasses() {
        return handlers;
    }

    @Override
    public final void subscribe(Class<? extends RequestHandler> subscriberClass) {
        this.handlers.add(subscriberClass);
    }

    @Override
    public final void subscribeAll(Set<Class<? extends RequestHandler>> subscriberSet) {
        this.handlers.addAll(subscriberSet);
    }

    @Override
    public final void unsubscribe(Class<? extends RequestHandler> subscriberClass) {
        this.handlers.remove(subscriberClass);
    }

    @Override
    public final int getPort() {
        return getClass().getAnnotation(ServerComponent.class).port();
    }

    @Override
    public final String getHost() {
        return getClass().getAnnotation(ServerComponent.class).host();
    }
}
