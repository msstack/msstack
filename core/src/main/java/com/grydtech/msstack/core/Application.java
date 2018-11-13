package com.grydtech.msstack.core;

import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.services.TopicMessagesConsumer;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;
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
        Set<Class<? extends Message>> messages = classPathScanner.getSubTypesOf(Message.class);
        Set<Class<? extends Entity>> entities = classPathScanner.getSubTypesOf(Entity.class);

        // Connectors
        final MessageBusConnector messageBusConnector = MessageBusConnector.getInstance();
        final SnapshotConnector snapshotConnector = SnapshotConnector.getInstance();

        entities.forEach(en -> {
            final TopicMessagesConsumer topicMessagesConsumer = new TopicMessagesConsumer(en);
            handlers.forEach(topicMessagesConsumer::registerHandler);
            messages.forEach(topicMessagesConsumer::registerMessage);
            messageBusConnector.attach(en, topicMessagesConsumer);
        });

        // Start connectors
        messageBusConnector.connect();
        snapshotConnector.connect();
    }
}
