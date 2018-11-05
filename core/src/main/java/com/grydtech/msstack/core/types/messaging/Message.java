package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.connectors.eventstore.EventStoreConnector;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Unique;

/**
 * Base class for all Messaging in MSStack
 *
 * @param <I> Identifier Type
 * @param <P> Payload Type
 */
public interface Message<I, P> extends Unique<I> {

    String getTopic();

    P getPayload();

    void setPayload(P payload);

    default void emit() {
        MessageBusConnector.getInstance().push(this);
        if (this instanceof Event) {
            EventStoreConnector.getInstance().push((Event) this);
        }
    }
}
