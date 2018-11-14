package com.grydtech.msstack.core.connectors.messagebus;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;
import com.grydtech.msstack.core.services.MessageConsumer;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

import java.util.Map;

/**
 * Base class for plugging in Message Brokers
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class MessageBusConnector implements IConnector {

    @InjectInstance
    private static MessageBusConnector instance;

    public static MessageBusConnector getInstance() {
        return instance;
    }

    public abstract void attach(Class<? extends Entity> entityClass, MessageConsumer consumer);

    public abstract void detach(Class<? extends Entity> entityClass);

    /**
     * Accepts a Message and notifies the subscribers
     *
     * @param message Published Event
     */
    public abstract void push(Message message, Map metadata);
}
