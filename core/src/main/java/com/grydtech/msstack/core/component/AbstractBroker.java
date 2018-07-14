package com.grydtech.msstack.core.component;

import java.util.Set;

public interface AbstractBroker<B> {

    /**
     * Subscribes a Class to be notified of Published Objects
     *
     * @param subscriberClass Subscribed Class
     */
    @SuppressWarnings("unused")
    void subscribe(Class<? extends B> subscriberClass);

    /**
     * Subscribes al Classes to be notified of Published Objects
     *
     * @param subscriberSet Set of Subscribed Classes
     */
    @SuppressWarnings("unused")
    void subscribeAll(Set<Class<? extends B>> subscriberSet);

    /**
     * Unsubscribes a Class from being notified of Published Objects
     *
     * @param subscriberClass Subscribed Class
     */
    @SuppressWarnings("unused")
    void unsubscribe(Class<? extends B> subscriberClass);

    /**
     * Starts the broker
     *
     * @throws Exception Exception if starting fails
     */
    void start() throws Exception;
}
