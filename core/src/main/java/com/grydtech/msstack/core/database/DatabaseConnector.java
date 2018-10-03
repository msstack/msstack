package com.grydtech.msstack.core.database;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Value;
import com.grydtech.msstack.core.configuration.ApplicationConfiguration;

/**
 * Created by dileka on 9/19/18.
 */
@FrameworkComponent
public abstract class DatabaseConnector<T> {
    
    @Value
    protected static ApplicationConfiguration applicationConfiguration;
    
    @AutoInjected
    private static DatabaseConnector instance;
    
    protected DatabaseConnector() {
    }
    
    public static DatabaseConnector getInstance() {
        return instance;
    }
    
    public abstract void putKey(T key);
    
    public abstract T getValue(String key);
    
}
