package com.grydtech.msstack.core.connectors.snapshot;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by dileka on 9/19/18.
 */
@FrameworkComponent
public abstract class SnapshotConnector<K, V> implements IConnector {

    @InjectInstance
    private static SnapshotConnector instance;

    public static SnapshotConnector getInstance() {
        return instance;
    }

    public abstract void put(K key, V value);

    public abstract void put(Map<K, V> data) throws IOException;

    public abstract V get(K key);

    public abstract V getFromSnapshot(K key);

    public abstract void updateSnapshot();

    public abstract void delete(K key);

}
