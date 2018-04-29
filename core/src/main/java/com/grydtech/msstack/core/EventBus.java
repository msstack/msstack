package com.grydtech.msstack.core;

import com.google.common.base.CaseFormat;
import com.grydtech.msstack.common.util.ClassPathScanner;
import com.grydtech.msstack.common.util.JsonConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EventBus {

    private static final Logger LOGGER = Logger.getLogger(EventBus.class.toGenericString());
    private static MessageBroker messageBroker;
    private static ExecutorService executorService;

    static {
        ClassPathScanner classPathScanner = new ClassPathScanner("com.grydtech.msstack.transport");
        Class<? extends MessageBroker> messageBrokerClass = classPathScanner.getSubTypesOf(MessageBroker.class).iterator()
                .next();
        try {
            messageBroker = messageBrokerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // ToDo: Temporary implementation (corePoolSize, maximumPoolSize, keepAliveTime hardcoded)
        executorService = new ThreadPoolExecutor(5, 10, 5000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    private EventBus() {
    }

    public static void publish(Event event) {
        executorService.submit(() -> {
            String topic = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, event.getClass().getSimpleName());
            String message = JsonConverter.toJsonString(event);
            EventBus.messageBroker.publish(topic, message);
        });
    }

    // ToDo: Need to change implementation (maybe use rxjave)
    public static void registerHandler(Class<? extends EventHandler> handlerClass) {
        messageBroker.registerHandler(handlerClass);
    }

    // ToDo: Need to change implementation (maybe use rxjave)
    public static void unregisterHandler(Class<? extends EventHandler> handlerClass) {
        messageBroker.unregisterHandler(handlerClass);
    }
}
