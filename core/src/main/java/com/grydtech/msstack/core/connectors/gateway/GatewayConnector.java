package com.grydtech.msstack.core.connectors.gateway;

import com.grydtech.msstack.annotation.InjectConfiguration;
import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.connectors.IConnector;

public abstract class GatewayConnector implements IConnector {

    @InjectConfiguration
    protected static ApplicationConfiguration applicationConfiguration;

    @InjectInstance
    private static GatewayConnector instance;

    public static GatewayConnector getInstance() {
        return instance;
    }
}
