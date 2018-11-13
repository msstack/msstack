package com.grydtech.msstack.core.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.util.MessageBusUtils;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {
    @JsonIgnore
    public abstract UUID getEntityId();

    public abstract void apply(Event event);

    public final String getTopic() {
        return MessageBusUtils.getTopicByEntityClass(getClass());
    }
}
