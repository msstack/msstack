package com.grydtech.msstack.core.types;

import com.grydtech.msstack.core.types.messaging.Event;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {
    private UUID id;

    public abstract void apply(Event event);
}
