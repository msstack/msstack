package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.transport.kafka.util.InvokeHelper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class KafkaConsumerService extends KafkaService {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class.toGenericString());
    private final KafkaConsumer<String, String> kafkaConsumer;
    private Map<String, HashSet<String>> topicClassMap;

    public KafkaConsumerService(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfiguration.getMessageBusConfiguration().getBootstrap());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConfiguration.getServiceRegistryConfiguration().getName());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationConfiguration.getServiceRegistryConfiguration().getId());
        this.kafkaConsumer = new KafkaConsumer<>(properties);
    }

    public void setTopicClassMap(Map<String, HashSet<String>> topicClassMap) {
        this.topicClassMap = topicClassMap;
    }

    @Override
    public void start() {
        LOGGER.info("Starting scheduled event consumer");
        kafkaConsumer.subscribe(this.topicClassMap.keySet());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(applicationConfiguration.getMessageBusConfiguration().getInterval());
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.topic() + "::" + record.key();

                    if (topicClassMap.containsKey(key)) {
                        final HashSet<String> classNames = topicClassMap.get(key);
                        classNames.forEach(s -> {
                            try {
                                Class<? extends Handler> h = Class.forName(s).asSubclass(Handler.class);
                                InvokeHelper.invokeHandleMethod(h, record.value());
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }, 6000, applicationConfiguration.getMessageBusConfiguration().getInterval());
        LOGGER.info("Scheduled event consumer started");
    }
}
