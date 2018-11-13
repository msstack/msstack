package com.grydtech.msstack.core.connectors.registry;

import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

/**
 * Connector for Service Registry and Discovery
 */
public abstract class RegistryConnector implements IConnector {

    @InjectInstance
    private static RegistryConnector instance;

    public static RegistryConnector getInstance() {
        return instance;
    }

    /**
     * <p>Registers the current service instance in the registry.</p>
     * <p><b>It also register handlers for member update / remove events to configure partitions</b></p>
     */
    @Override
    public abstract void connect();

    /**
     * Gracefully unregisters the current service instance from the registry.
     */
    @Override
    public abstract void disconnect();
}
