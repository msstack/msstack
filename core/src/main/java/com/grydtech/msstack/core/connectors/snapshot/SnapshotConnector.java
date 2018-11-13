package com.grydtech.msstack.core.connectors.snapshot;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

/**
 * Created by dileka on 9/19/18.
 */
@FrameworkComponent
public abstract class SnapshotConnector implements IConnector {

    @InjectInstance
    private static SnapshotConnector instance;

    public static SnapshotConnector getInstance() {
        return instance;
    }

    public abstract void put(String key, Object value);

    public abstract <T> T get(String key, Class<T> outputClass);

    public abstract void delete(String key);

}
