package com.grydtech.msstack.core.types.messaging;

import java.util.UUID;

@SuppressWarnings("unused")
public abstract class Event<E> implements Message<UUID, E> {

    private UUID uuid;
    private E payload;

    @Override
    public final UUID getId() {
        return uuid;
    }

    @Override
    public final Event<E> setId(UUID id) {
        this.uuid = id;
        return this;
    }

    @Override
    public final E getPayload() {
        return payload;
    }

    @Override
    public final Event<E> setPayload(E payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public final String getTopic() {
        try {
            return getClass().getMethod("getPayload").getReturnType().getSimpleName();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
