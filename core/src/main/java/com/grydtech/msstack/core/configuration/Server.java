package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class Server {
    private String name = null; // group services in cluster
    private String host = "http://localhost";
    private int port = 8080;

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
