package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.types.Entity;

import java.util.UUID;

/**
 * Command Class
 */
public abstract class Command<E extends Entity> extends Message<E> {
    public Command(UUID entityId) {
        super(entityId);
    }
}
