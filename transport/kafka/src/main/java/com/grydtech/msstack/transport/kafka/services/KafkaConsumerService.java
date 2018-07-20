package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.core.configuration.ApplicationConfiguration;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.transport.kafka.util.InvokeHelper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.logging.Logger;

public class KafkaConsumerService extends KafkaService {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class.toGenericString());
    private final KafkaConsumer<String, String> kafkaConsumer;
    private Map<String, Class<? extends EventHandler>> handlerClassMap;
    private List<String> streams;

    public KafkaConsumerService(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfiguration.getBroker().getBootstrap());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConfiguration.getServer().getName());
        this.kafkaConsumer = new KafkaConsumer<>(properties);
    }

    public void setHandlerClassMap(Map<String, Class<? extends EventHandler>> handlerClassMap) {
        this.handlerClassMap = handlerClassMap;
    }

    public void setStreams(List<String> streams) {
        this.streams = streams;
    }

    @Override
    public void start() {
        LOGGER.info("Starting scheduled event consumer");
        kafkaConsumer.subscribe(this.streams);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(applicationConfiguration.getBroker().getInterval());
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.topic() + "::" + record.key();

                    if (handlerClassMap.containsKey(key)) {
                        Class<? extends EventHandler> handlerClass = handlerClassMap.get(key);
                        InvokeHelper.invokeHandleMethod(handlerClass, record.value());
                    }
                }
            }
        }, 6000, applicationConfiguration.getBroker().getInterval());
        LOGGER.info("Scheduled event consumer started");
    }
}
