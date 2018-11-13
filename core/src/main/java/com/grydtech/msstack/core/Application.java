package com.grydtech.msstack.core;

import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.services.TopicMessagesConsumer;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.util.ClassPathScanner;

import java.util.Set;

/**
 * Base class for an application in MSStack
 * The class path should have exactly one implementation of this class
 */
public abstract class Application {

    /**
     * Starts the Application
     *
     * @throws Exception If an exception occurs that is not handled within the framework
     */
    public final void start() throws Exception {
        ClassPathScanner classPathScanner = new ClassPathScanner(getClass().getPackage().getName());
        Set<Class<? extends Handler>> handlers = classPathScanner.getSubTypesOf(Handler.class);
        Set<Class<? extends Event>> events = classPathScanner.getSubTypesOf(Event.class);
        Set<Class<? extends Entity>> entities = classPathScanner.getSubTypesOf(Entity.class);

        // Connectors
        final MessageBusConnector messageBusConnector = MessageBusConnector.getInstance();
        final SnapshotConnector snapshotConnector = SnapshotConnector.getInstance();

        entities.forEach(en -> {
            final TopicMessagesConsumer topicMessagesConsumer = new TopicMessagesConsumer(en);
            handlers.forEach(topicMessagesConsumer::registerHandler);
            events.forEach(topicMessagesConsumer::registerEvent);
            messageBusConnector.attach(en, topicMessagesConsumer);
        });

        try {
            // Start connectors
            messageBusConnector.connect();
            snapshotConnector.connect();

        } finally {
            // Cleanup before termination
            messageBusConnector.disconnect();
            snapshotConnector.disconnect();
        }
    }
}
