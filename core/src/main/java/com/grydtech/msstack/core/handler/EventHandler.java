package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.BasicEvent;

@SuppressWarnings("unused")
public interface EventHandler<E extends BasicEvent> {

    void handle(E event);
}
