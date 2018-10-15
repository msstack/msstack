package com.grydtech.msstack.core.types.messaging;

import java.util.UUID;

public abstract class Command implements Message<UUID, String>, Metadata {

    private UUID id;
    private String payload;

    @Override
    public final String getTopic() {
        try {
            return getClass().getDeclaredMethod("getPayload").getReturnType().getSimpleName() + "::in";
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public final String getPayload() {
        return payload;
    }

    @Override
    public final Command setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public final UUID getId() {
        return id;
    }

    @Override
    public final Command setId(UUID id) {
        this.id = id;
        return this;
    }
}
