package com.grydtech.msstack.core.types.messaging;

import lombok.Data;

import java.util.UUID;

/**
 * Query Class
 *
 * @param <P> Payload Type
 * @param <M> Metadata Type
 */
@Data
public class Query<P, M> implements Request<P, M> {

    private UUID id;
    private P payload;
    private M metadata;

    @Override
    public final String getTopic() {
        try {
            return getClass().getDeclaredMethod("getPayload").getReturnType().getSimpleName() + "::in";
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
