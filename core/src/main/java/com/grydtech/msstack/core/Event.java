package com.grydtech.msstack.core;

import com.grydtech.msstack.core.component.EventBroker;

@SuppressWarnings("unused")
public abstract class Event {

    public final void emit() {
        EventBroker.getInstance().publish(this);
    }
}
