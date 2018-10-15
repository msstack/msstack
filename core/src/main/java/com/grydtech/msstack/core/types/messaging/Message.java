package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.connectors.eventstore.EventStoreConnector;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Unique;

public interface Message<I, M> extends Unique<I> {

    String getTopic();

    M getPayload();

    Message<I, M> setPayload(M payload);

    default void emit() {
        MessageBusConnector.getInstance().push(this);
        if (this instanceof Event) {
            EventStoreConnector.getInstance().push((Event) this);
        }
    }
}
