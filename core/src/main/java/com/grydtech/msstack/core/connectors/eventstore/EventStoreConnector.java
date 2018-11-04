package com.grydtech.msstack.core.connectors.eventstore;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

import java.util.List;

/**
 * Connector for Event Store.
 * Created by dileka on 9/19/18.
 *
 * @param <K> Key Type
 * @param <V> Value Type
 */
@FrameworkComponent
public abstract class EventStoreConnector<K, V> implements IConnector {

    @InjectInstance
    private static EventStoreConnector instance;

    public static EventStoreConnector getInstance() {
        return instance;
    }

    public abstract void push(V event);

    public abstract V get(K uuid);

    public abstract List<V> get(String topic);
}
