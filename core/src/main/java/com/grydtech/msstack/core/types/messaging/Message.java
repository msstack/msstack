package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.util.EntityUtils;
import lombok.Data;

import java.util.UUID;

/**
 * Base class for all Messaging in MSStack
 */
@Data
public abstract class Message<E extends Entity> {
    private final UUID id;
    private UUID entityId;

    public Message(UUID entityId) {
        this.id = UUID.randomUUID();
        this.entityId = entityId;
    }

    @JsonIgnore
    public abstract Class<E> getEntityClass();

    public final void emit(UUID flowId) {
        MessageBusConnector.getInstance().push(this);
    }

    public final String getTopic() {
        return EntityUtils.getTopic(this.getEntityClass());
    }
}
