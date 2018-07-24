package com.grydtech.msstack.core.configuration;

@SuppressWarnings("all")
public final class MessageBrokerConfiguration {
    private String bootstrap = null;
    private Integer retries = Integer.MAX_VALUE;
    private String acks = "all";
    private int interval = 100;

    public String getBootstrap() {
        return bootstrap;
    }

    public Integer getRetries() {
        return retries;
    }

    public String getAcks() {
        return acks;
    }

    public int getInterval() {
        return interval;
    }
}
