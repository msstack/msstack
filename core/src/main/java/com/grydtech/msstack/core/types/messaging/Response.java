package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.types.Entity;

import java.util.UUID;

/**
 * Response Class
 */
public abstract class Response<E extends Entity> extends Message<E> {
    public Response(UUID entityId) {
        super(entityId);
    }
}
