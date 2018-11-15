package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.util.MessageBusUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Event<E extends Entity> extends Message<E> {

    @JsonIgnore
    public final String getTopic() {
        return MessageBusUtils.getTopicByEntityClass(this.getEntityClass());
    }

    public final void emit(UUID flowId) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "EVENT");
        map.put("flowId", flowId);

        MessageBusConnector.getInstance().push(this, map);
    }
}
