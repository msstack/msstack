package com.grydtech.msstack.core;

import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.core.component.RequestBroker;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.core.handler.RequestHandler;
import com.grydtech.msstack.core.serviceregistry.MembershipProtocol;
import com.grydtech.msstack.util.ClassPathScanner;

import java.util.Set;

/**
 * Base class for a Microservice Application created using MSStack.
 * All applications should have one class that inherits this in the module root
 */
public abstract class MicroserviceApplication {

    private final Set<Class<? extends RequestHandler>> requestHandlers;
    private final Set<Class<? extends EventHandler>> eventHandlers;

    public MicroserviceApplication() {
        ClassPathScanner classPathScanner = new ClassPathScanner(getClass().getPackage().getName());
        eventHandlers = classPathScanner.getSubTypesOf(EventHandler.class);
        requestHandlers = classPathScanner.getSubTypesOf(RequestHandler.class);
    }

    public String getServiceName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the set of request handler classes in the classpath
     *
     * @return Set of Request Handler Classes
     */
    public final Set<Class<? extends RequestHandler>> getRequestHandlers() {
        return requestHandlers;
    }

    /**
     * Returns the set of event handler classes in the classpath
     *
     * @return Set of BasicEvent Handler Classes
     */
    public final Set<Class<? extends EventHandler>> getEventHandlers() {
        return eventHandlers;
    }

    /**
     * Runs the Microservice
     *
     * @throws Exception If an exception occurs that is not handled within the framework
     */
    public final void run() throws Exception {

        // Brokers
        final EventBroker eventBroker = EventBroker.getInstance();
        final RequestBroker requestBroker = RequestBroker.getInstance();
        final MembershipProtocol membershipProtocol = MembershipProtocol.getInstance();

        try {
            // Configure Membership Protocol (Assuming ZK up and running in 2181)
            membershipProtocol.setConnectionString("127.0.0.1:2181");

            // Register BasicEvent Handlers
            this.eventHandlers.forEach(eventBroker::subscribe);

            // Register Request Handlers
            this.requestHandlers.forEach(requestBroker::subscribe);

            // Start BasicEvent Broker
            eventBroker.start();

            // Start Request Broker
            requestBroker.start();

            // Start Service Discovery
            membershipProtocol.start();

            // Register Service
            membershipProtocol.registerMember(getServiceName(), requestBroker.getHost(), requestBroker.getPort());

            // Optional

        } finally {
            // Cleanup before termination
            eventBroker.flush();
        }
    }
}
