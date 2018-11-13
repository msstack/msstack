package com.grydtech.msstack.core.types.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grydtech.msstack.core.types.Entity;

public abstract class Response<E extends Entity> extends Message<E> {

    @JsonIgnore
    public final String getTopic() {
        return "response";
    }
}
