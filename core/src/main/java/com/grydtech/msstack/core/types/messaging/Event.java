package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.types.Entity;

import java.util.UUID;

/**
 * Event Class
 */
public abstract class Event<E extends Entity> extends Message<E> {
    public Event(UUID entityId) {
        super(entityId);
    }
}
