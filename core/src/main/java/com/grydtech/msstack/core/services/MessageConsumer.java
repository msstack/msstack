package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Event;

import java.util.function.Consumer;

public interface MessageConsumer extends Consumer<String> {

    void registerEvent(Class<? extends Event> event);

    void registerHandler(Class<? extends Handler> handler);
}
