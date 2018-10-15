package com.grydtech.msstack.core.connectors.messagebus;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectConfiguration;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.connectors.IConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.util.HandlerUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for plugging in Message Brokers
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class MessageBusConnector implements IConnector {

    private static final Logger LOGGER = Logger.getLogger(MessageBusConnector.class.toGenericString());

    @InjectConfiguration
    protected static ApplicationConfiguration applicationConfiguration;

    @InjectInstance
    private static MessageBusConnector instance;

    private final Map<String, HashSet<String>> topicClassSetMap;

    protected MessageBusConnector() {
        topicClassSetMap = new HashMap<>();
    }

    public static MessageBusConnector getInstance() {
        return instance;
    }

    public final void attach(Class<? extends Handler> handlerClass) {
        final String topic = HandlerUtils.getTopic(handlerClass);
        final String className = handlerClass.getName();
        final Map<String, HashSet<String>> map = this.topicClassSetMap;
        if (topic != null) {
            if (map.containsKey(topic)) {
                map.get(topic).add(className);
            } else {
                map.put(topic, Stream.of(className).collect(Collectors.toCollection(HashSet::new)));
            }
            LOGGER.info(String.format("[TOPIC][%s] = %s | attached", topic, className));
        } else {
            LOGGER.warning(String.format("[TOPIC][%s] = %s | not attached", null, className));
        }
    }

    public final void detach(Class<? extends Handler> handlerClass) {
        final String topic = HandlerUtils.getTopic(handlerClass);
        final String className = handlerClass.getName();
        if (topic != null) {
            this.topicClassSetMap.get(topic).removeIf(c -> c.equals(className));
            LOGGER.info(String.format("[TOPIC][%s] -> %s | detached", topic, className));
        } else {
            LOGGER.warning(String.format("[TOPIC][%s] -> %s | not detached", null, className));
        }
    }

    protected final Map<String, HashSet<String>> getTopicClassSetMap() {
        return topicClassSetMap;
    }

    /**
     * Accepts a Message and notifies the subscribers
     *
     * @param message Published Event
     */
    public abstract void push(Message message);
}
