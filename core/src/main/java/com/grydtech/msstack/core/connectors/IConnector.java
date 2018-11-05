package com.grydtech.msstack.core.connectors;

import java.io.IOException;

public interface IConnector {

    /**
     * Connects to a running instance
     */
    void connect() throws IOException;

    /**
     * Performs cleanup and disconnects from running instance
     */
    void disconnect() throws IOException;
}
