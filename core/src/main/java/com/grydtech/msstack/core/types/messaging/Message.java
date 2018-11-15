package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.types.Entity;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Message<E extends Entity> {
    @JsonIgnore
    public abstract UUID getEntityId();

    @JsonIgnore
    public abstract Class<E> getEntityClass();

    @JsonIgnore
    public abstract String getTopic();

    @JsonIgnore
    public final String getMessageName() {
        return getClass().getSimpleName().toLowerCase() + "_" + this.getEntityClass().getSimpleName().toLowerCase();
    }
}
