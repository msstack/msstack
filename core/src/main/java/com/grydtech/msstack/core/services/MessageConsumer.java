package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Message;

import java.util.function.Consumer;

public interface MessageConsumer extends Consumer<String> {

    void registerMessage(Class<? extends Message> message);

    void registerHandler(Class<? extends Handler> handler);
}
