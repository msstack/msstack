package com.grydtech.msstack.core.connectors.gateway;

import com.grydtech.msstack.annotation.InjectInstance;
import com.grydtech.msstack.core.connectors.IConnector;

public abstract class GatewayConnector implements IConnector {

    @InjectInstance
    private static GatewayConnector instance;

    private GatewayConnector() {
    }

    public static GatewayConnector getInstance() {
        return instance;
    }
}
