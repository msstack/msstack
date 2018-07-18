package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class Server {
    private String id = null; // uniquely identify service among the cluster (eg: order-service-1, order-service-2)
    private String group = null; // group services in cluster
    private String host = "http://localhost";
    private int port = 8080;

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
