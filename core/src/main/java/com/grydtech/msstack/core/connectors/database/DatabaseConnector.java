package com.grydtech.msstack.core.connectors.database;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectConfiguration;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.connectors.IConnector;

/**
 * Created by dileka on 9/19/18.
 */
@FrameworkComponent
public abstract class DatabaseConnector<T> implements IConnector {

    @InjectConfiguration
    protected static ApplicationConfiguration applicationConfiguration;

    @InjectInstance
    private static DatabaseConnector instance;

    protected DatabaseConnector() {
    }

    public static DatabaseConnector getInstance() {
        return instance;
    }

    public abstract void putKey(T key, T value);

    public abstract T getValue(String key);

}
