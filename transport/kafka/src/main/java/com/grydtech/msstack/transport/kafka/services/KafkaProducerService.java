package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.getName());
    private static final String bootstrapServers;
    private static final int acks;
    private static final int retries;
    private static final int pollingDelay;
    private static final int pollingInterval;

    static {
        bootstrapServers = ConfigurationProperties.get(ConfigKey.CONFIG_BOOTSTRAP);
        acks = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_ACKS));
        retries = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_RETRIES));
        pollingDelay = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_DELAY));
        pollingInterval = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_INTERVAL));
    }

    private final KafkaProducer<String, String> kafkaProducer;

    public KafkaProducerService() {
        Properties properties = generateProperties();
        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    private Properties generateProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        return properties;
    }

    public void publish(String topic, String key, String messageName, String metadata, String eventData) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, messageName + "::" + metadata + "::" + eventData);
        this.kafkaProducer.send(record);
    }

    public void flush() {
        this.kafkaProducer.flush();
    }

    public void start() {
        LOGGER.info("Starting scheduled event publisher");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                kafkaProducer.flush();
            }
        }, pollingDelay, pollingInterval);
        LOGGER.info("Scheduled event publisher started");
    }
}
