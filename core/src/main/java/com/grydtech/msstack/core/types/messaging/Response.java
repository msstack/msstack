package com.grydtech.msstack.core.types.messaging;

import java.util.UUID;

public abstract class Response implements Message<UUID, String> {

    private UUID id;
    private String payload;

    @Override
    public final String getTopic() {
        try {
            return getClass().getDeclaredMethod("getPayload").getReturnType().getSimpleName() + "::out";
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Response setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Response setId(UUID id) {
        this.id = id;
        return this;
    }
}
