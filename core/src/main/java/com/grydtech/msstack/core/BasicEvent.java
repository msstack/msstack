package com.grydtech.msstack.core;

import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.core.eventstore.EventStoreConnector;

@SuppressWarnings("unused")
public abstract class BasicEvent {

    public final void emit() {
        EventBroker.getInstance().publish(this);
        EventStoreConnector.getInstance().putEvent(this);
    }
}
