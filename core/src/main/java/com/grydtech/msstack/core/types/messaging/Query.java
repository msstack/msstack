package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.types.Entity;

import java.util.UUID;

/**
 * Query Class
 */
public abstract class Query<E extends Entity> extends Message<E> {
    public Query(UUID entityId) {
        super(entityId);
    }
}
