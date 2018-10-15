package com.grydtech.msstack.core.handler;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.core.types.QueryArgs;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Handler<E extends Entity, M extends Message> extends Consumer<M> {

    E get(QueryArgs<? super Entity> args);

    @Override
    void accept(M m);

    @Override
    Consumer<M> andThen(Consumer<? super M> after);
}
