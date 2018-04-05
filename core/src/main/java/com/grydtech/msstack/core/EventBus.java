package com.grydtech.msstack.core;

import org.reflections.Reflections;

public final class EventBus {
    private static MessageBroker messageBroker;

    static {
        Reflections reflections = new Reflections("com.grydtech.msstack.transport");
        Class<? extends MessageBroker> messageBrokerClass = reflections.getSubTypesOf(MessageBroker.class).iterator().next();
        try {
            messageBroker = messageBrokerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private EventBus() {
    }

    public static void publish(Event event) {
        messageBroker.publish(event);
    }

    public static void registerHandler(Class<? extends EventHandler> handlerClass) {
        messageBroker.registerHandler(handlerClass);
    }

    public static void unregisterHandler(Class<? extends EventHandler> handlerClass) {
        messageBroker.unregisterHandler(handlerClass);
    }
}
