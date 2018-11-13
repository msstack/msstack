package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.util.MessageBusUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for all Messaging in MSStack
 */
@Data
public abstract class Message<E extends Entity> {
    @JsonIgnore
    public abstract UUID getEntityId();

    @JsonIgnore
    public abstract Class<E> getEntityClass();

    @JsonIgnore
    public final String getTopic() {
        return MessageBusUtils.getTopicByEntityClass(this.getEntityClass());
    }

    @JsonIgnore
    public final String getMessageName() {
        return getClass().getSimpleName().toLowerCase() + "_" + this.getEntityClass().getSimpleName().toLowerCase();
    }

    public final void emit(UUID flowId) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "EVENT");
        map.put("flowId", flowId);

        MessageBusConnector.getInstance().push(this, map);
    }
}
