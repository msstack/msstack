package com.grydtech.msstack.core.connectors.messagebus;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Message;

/**
 * Base class for plugging in Message Brokers
 * The first instance in the classpath that extends this class is injected in
 */
@FrameworkComponent
public abstract class MessageBusConnector implements IConnector {

    @InjectInstance
    private static MessageBusConnector instance;

    protected MessageBusConnector() {
    }

    public static MessageBusConnector getInstance() {
        return instance;
    }

    public abstract void attach(Handler handler);

    public abstract void detach(Handler handler);

    /**
     * Accepts a Message and notifies the subscribers
     *
     * @param message Published Event
     */
    public abstract void push(Message message);
}
