package com.grydtech.msstack.sample;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Event;

import java.util.UUID;

public final class SampleEntity extends Entity {

    public SampleEntity(UUID id) {
        super(id);
    }

    @Override
    public void apply(Event event) {

    }
}
