package com.grydtech.msstack.transport.kafka.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.transport.kafka.util.InvokeHelper;
import com.grydtech.msstack.util.JsonConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;
import java.util.logging.Logger;

public class KafkaConsumerService implements KafkaService {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class.toGenericString());
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final int pollIntervalMs;
    private Map<String, Class<? extends EventHandler>> handlerClassMap;
    private List<String> streams;

    public KafkaConsumerService(Properties properties) {
        this.kafkaConsumer = new KafkaConsumer<>(properties);
        this.pollIntervalMs = Integer.parseInt((String) properties.get("poll.interval.ms"));
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
                ConsumerRecords<String, String> records = kafkaConsumer.poll(pollIntervalMs);
                for (ConsumerRecord<String, String> record : records) {
                    JsonNode jsonObject = JsonConverter.getJsonNode(record.value()).orElse(null);
                    assert jsonObject != null;
                    String key = record.topic() + "::" + jsonObject.get("event").asText();

                    if (jsonObject.has("data") && handlerClassMap.containsKey(key)) {
                        Class<? extends EventHandler> handlerClass = handlerClassMap.get(key);
                        InvokeHelper.invokeHandleMethod(handlerClass, jsonObject.get("data"));
                    }
                }
            }
        }, 6000, pollIntervalMs);
        LOGGER.info("Scheduled event consumer started");
    }
}
