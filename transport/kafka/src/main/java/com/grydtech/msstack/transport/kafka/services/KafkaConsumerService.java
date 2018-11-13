package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.config.DataKey;
import com.grydtech.msstack.config.DataProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class KafkaConsumerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class.getName());
    private static final String bootstrapServers;
    private static final int partitions;
    private static final int groupId;
    private static final String clientId;
    private static final int pollingInterval;
    private static final int pollingDelay;

    static {
        bootstrapServers = ConfigurationProperties.get(ConfigKey.CONFIG_BOOTSTRAP);
        partitions = Integer.parseInt(DataProperties.get(DataKey.DATA_ENTITY_PARTITIONS));
        groupId = new Random().nextInt(partitions);
        pollingInterval = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_INTERVAL));
        pollingDelay = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_DELAY));
        clientId = UUID.randomUUID().toString();
    }

    private final KafkaConsumer<String, String> kafkaConsumer;

    private Map<String, Consumer<String>> consumers;

    public KafkaConsumerService() {
        Properties properties = generateProperties();
        this.kafkaConsumer = new KafkaConsumer<>(properties);
    }

    private Properties generateProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        return properties;
    }

    public void setConsumers(Map<String, Consumer<String>> consumers) {
        this.consumers = consumers;
    }

    public void start() {
        LOGGER.info("Starting scheduled event consumer");
        kafkaConsumer.subscribe(consumers.keySet());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(pollingInterval);
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    final Consumer<String> consumer = consumers.get(topic);
                    consumer.accept(record.value());
                }
            }
        }, pollingDelay, pollingInterval);
        LOGGER.info("Scheduled event consumer started");
    }

    public void stop() {
        kafkaConsumer.unsubscribe();
    }
}
