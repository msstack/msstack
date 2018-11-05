package com.grydtech.msstack.core;

import com.grydtech.msstack.core.connectors.eventstore.EventStoreConnector;
import com.grydtech.msstack.core.connectors.gateway.GatewayConnector;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.connectors.registry.RegistryConnector;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.util.DIUtils;
import com.grydtech.msstack.util.HandlerUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for an application in MSStack
 * The class path should have exactly one implementation of this class
 */
public abstract class Application {

    private final Set<? extends Handler> handlers;

    public Application() {
        handlers = DIUtils.getScanner()
                .getSubTypesOf(Handler.class)
                .parallelStream()
                .map(HandlerUtils::instantiate)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Starts the Application
     *
     * @throws Exception If an exception occurs that is not handled within the framework
     */
    public final void start() throws Exception {

        // Connectors
        final EventStoreConnector eventStoreConnector = EventStoreConnector.getInstance();
        final GatewayConnector gatewayConnector = GatewayConnector.getInstance();
        final MessageBusConnector messageBusConnector = MessageBusConnector.getInstance();
        final RegistryConnector registryConnector = RegistryConnector.getInstance();
        final SnapshotConnector snapshotConnector = SnapshotConnector.getInstance();

        try {
            // Register Handlers
            this.handlers.forEach(messageBusConnector::attach);

            // Start connectors
            eventStoreConnector.connect();
            gatewayConnector.connect();
            messageBusConnector.connect();
            registryConnector.connect();
            snapshotConnector.connect();

            // Block until user terminates
            this.wait();

        } finally {
            // Cleanup before termination
            eventStoreConnector.disconnect();
            gatewayConnector.connect();
            messageBusConnector.disconnect();
            registryConnector.disconnect();
            snapshotConnector.disconnect();
        }
    }
}
