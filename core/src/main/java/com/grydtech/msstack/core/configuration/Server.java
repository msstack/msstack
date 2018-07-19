package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class Server {
    private String id = null; // id of service node
    private String name = null; // group services in cluster
    private String host = "localhost";
    private int port = 8080;

    public String getId() {
        return id;
    }

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
