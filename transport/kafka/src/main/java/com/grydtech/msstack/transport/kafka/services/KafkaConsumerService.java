package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.core.connectors.messagebus.ConsumerMessage;
import com.grydtech.msstack.core.connectors.messagebus.PartitionMetaData;
import com.grydtech.msstack.core.services.MessageConsumer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class KafkaConsumerService {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class.getName());
    private static final String bootstrapServers;
    private static final String groupId;
    private static final int pollingInterval;

    static {
        bootstrapServers = ConfigurationProperties.get(ConfigKey.CONFIG_BOOTSTRAP);
        groupId = ConfigurationProperties.get(ConfigKey.SERVICE_NAME);
        pollingInterval = Integer.parseInt(ConfigurationProperties.get(ConfigKey.BUS_INTERVAL));
    }

    private final KafkaConsumer<String, String> kafkaConsumer;

    private Map<String, MessageConsumer> consumers;

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
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        return properties;
    }

    public void setConsumers(Map<String, MessageConsumer> consumers) {
        this.consumers = consumers;
    }

    public void start() {
        LOGGER.info("Starting scheduled event consumer");
        kafkaConsumer.subscribe(consumers.keySet());
        LOGGER.info("Topic list: " + consumers.keySet().toString() + " subscribed");

        consumers.forEach((k, v) -> {
            ArrayList<PartitionMetaData> partitionMetaDataCollection = new ArrayList<>();

            kafkaConsumer.partitionsFor(k).forEach(p -> {
                OffsetAndMetadata offsetAndMetadata = kafkaConsumer.committed(new TopicPartition(p.topic(), p.partition()));

                PartitionMetaData partitionMetaData;
                if (offsetAndMetadata == null) {
                    partitionMetaData = new PartitionMetaData(p.partition(), 0);
                } else {
                    partitionMetaData = new PartitionMetaData(p.partition(), offsetAndMetadata.offset());
                }
                partitionMetaDataCollection.add(partitionMetaData);
            });

            v.setNextOffsetsToProcess(partitionMetaDataCollection);
        });

        kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());
        kafkaConsumer.commitSync();

        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(pollingInterval);
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    final MessageConsumer consumer = consumers.get(topic);
                    final ConsumerMessage consumerMessage = new ConsumerMessage(record.key(), record.value(), record.partition(), record.offset());

                    executorService.submit(() -> {
                        try {
                            consumer.accept(consumerMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    kafkaConsumer.commitSync();
                }
            }
        });
        LOGGER.info("Scheduled event consumer started");
    }

    public void stop() {
        kafkaConsumer.unsubscribe();
    }
}
