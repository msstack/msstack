package com.grydtech.msstack.core;

public abstract class Event {

    public void emit() {
        EventBus.publish(this);
    }
}
