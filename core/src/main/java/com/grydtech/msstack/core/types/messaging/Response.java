package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Response<E extends Entity> extends Message<E> {

    @JsonIgnore
    public final String getTopic() {
        return "response";
    }

    public final void emit(UUID flowId) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "RESPONSE");
        map.put("flowId", flowId);

        MessageBusConnector.getInstance().push(this, map);
    }
}
