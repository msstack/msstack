package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.util.MessageBusUtils;

public abstract class Command<E extends Entity> extends Message<E> {

    @JsonIgnore
    public final String getTopic() {
        return MessageBusUtils.getTopicByEntityClass(this.getEntityClass());
    }
}
