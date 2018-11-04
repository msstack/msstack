package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Handler<E extends Entity, M extends Message> extends Consumer<M> {

    /**
     * Get the entity that the handler needs to handle
     *
     * @return Entity
     */
    E get();

    @Override
    void accept(M m);

    @Override
    Consumer<M> andThen(Consumer<? super M> after);
}
