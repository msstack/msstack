package com.grydtech.msstack.core.types.messaging;

import lombok.Data;

import java.util.UUID;

/**
 * Response Class
 *
 * @param <P> Payload Type
 */
@Data
public class Response<P> implements Message<UUID, P> {

    private UUID id;
    private P payload;

    @Override
    public final String getTopic() {
        try {
            return getClass().getDeclaredMethod("getPayload").getReturnType().getSimpleName() + "::out";
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
