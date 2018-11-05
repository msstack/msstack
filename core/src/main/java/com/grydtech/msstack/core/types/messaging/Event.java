package com.grydtech.msstack.core.types.messaging;

import lombok.Data;

import java.util.UUID;

/**
 * Event Class
 *
 * @param <P> Payload Type
 */
@Data
public class Event<P> implements Message<UUID, P> {

    private UUID id;
    private P payload;

    @Override
    public final String getTopic() {
        try {
            return getClass().getMethod("getPayload").getReturnType().getSimpleName();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
