package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public interface Handler<E extends Entity, M extends Message> {
    void handle(M message, Map metadata, UUID flowId, E entity);
}
