package com.grydtech.msstack.transport.kafka.util;

import java.util.Properties;

/**
 * @implNote this is a temporary class to hold kafka configs
 * need to extract kafka config from application config file
 */
public final class ConfigHelper {

    public static final Properties PRODUCER_PROPERTIES;
    public static final Properties CONSUMER_PROPERTIES;

    static {
        PRODUCER_PROPERTIES = new Properties();
        PRODUCER_PROPERTIES.put("bootstrap.servers", "localhost:9092");
        PRODUCER_PROPERTIES.put("acks", "all");
        PRODUCER_PROPERTIES.put("retries", 0);
        PRODUCER_PROPERTIES.put("batch.size", 16384);
        PRODUCER_PROPERTIES.put("linger.ms", 1);
        PRODUCER_PROPERTIES.put("buffer.memory", 33554432);
        PRODUCER_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        PRODUCER_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        PRODUCER_PROPERTIES.put("flush.interval.ms", 100);

        CONSUMER_PROPERTIES = new Properties();
        CONSUMER_PROPERTIES.put("bootstrap.servers", "localhost:9092");
        CONSUMER_PROPERTIES.put("group.id", "msstack");
        CONSUMER_PROPERTIES.put("enable.auto.commit", "true");
        CONSUMER_PROPERTIES.put("auto.commit.interval.ms", "1000");
        CONSUMER_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        CONSUMER_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        PRODUCER_PROPERTIES.put("poll.interval.ms", 100);
    }

    private ConfigHelper() {
    }
}
