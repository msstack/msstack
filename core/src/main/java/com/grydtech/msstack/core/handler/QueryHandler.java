package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

/**
 * Interface for all Query Handlers
 *
 * @param <E> Entity Type
 * @param <M> ConsumerMessage Type
 */
public interface QueryHandler<E extends Entity, M extends Message> extends Handler<E, M> {
}
