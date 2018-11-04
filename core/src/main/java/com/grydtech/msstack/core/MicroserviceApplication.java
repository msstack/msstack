package com.grydtech.msstack.core;

import com.grydtech.msstack.core.connectors.eventstore.EventStoreConnector;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.connectors.serviceregistry.ServiceRegistryConnector;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.util.ClassPathScanner;

import java.util.Objects;
import java.util.Set;

/**
 * Base class for a MSStack application
 * The class path should have exactly one implementation of this class
 */
public abstract class MicroserviceApplication {

    private final Set<Class<? extends Handler>> handlers;

    public MicroserviceApplication() {
        ClassPathScanner classPathScanner = new ClassPathScanner(getClass().getPackage().getName());
        handlers = classPathScanner.getSubTypesOf(Handler.class);
    }

    /**
     * Returns the set of handler classes in the classpath
     *
     * @return Set of Handler Classes
     */
    public final Set<Class<? extends Handler>> getHandlers() {
        return handlers;
    }

    /**
     * Starts the Microservice Application
     *
     * @throws Exception If an exception occurs that is not handled within the framework
     */
    public final void start() throws Exception {

        // Connectors
        final SnapshotConnector databaseConnector = SnapshotConnector.getInstance();
        final EventStoreConnector eventStoreConnector = EventStoreConnector.getInstance();
        final MessageBusConnector messageBusConnector = MessageBusConnector.getInstance();
        final ServiceRegistryConnector serviceRegistryConnector = ServiceRegistryConnector.getInstance();

        try {
            // Register Handlers
            this.handlers.stream().map(h -> {
                try {
                    return h.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).forEach(messageBusConnector::attach);

            // Start connectors
            databaseConnector.connect();
            eventStoreConnector.connect();
            messageBusConnector.connect();
            serviceRegistryConnector.connect();

            // Block until user terminates
            this.wait();
        } finally {
            // Cleanup before termination
            databaseConnector.disconnect();
            eventStoreConnector.disconnect();
            messageBusConnector.disconnect();
            serviceRegistryConnector.disconnect();
        }
    }
}
