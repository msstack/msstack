package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.Event;

@SuppressWarnings("unused")
public interface EventHandler<E extends Event> {

    void handle(E event);
}
