package com.grydtech.msstack.configuration;

@SuppressWarnings("all")
public final class DatabaseConfiguration {

    private String databaseName = null;

    private String node = null;

    private String port = null;

    public String getPort() {
        return port;
    }

    public String getNode() {
        return node;
    }

    public String getDatabaseName() {

        return databaseName;
    }
}
