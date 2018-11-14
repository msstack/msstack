package com.grydtech.msstack.core.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.types.messaging.Event;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {
    private long version = 0;

    @JsonIgnore
    public abstract UUID getEntityId();

    public abstract void apply(Event event);

    public final void applyEventAndIncrementVersion(Event event) {
        this.apply(event);
        this.version++;
    }
}
