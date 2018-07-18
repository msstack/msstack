package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class ApplicationConfiguration {
    private Server server = null;
    private Broker broker = null;

    public Server getServer() {
        return server;
    }

    public Broker getBroker() {
        return broker;
    }
}
