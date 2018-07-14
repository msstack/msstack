package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.util.JsonConverter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class KafkaProducerService implements KafkaService {

    private final KafkaProducer<String, String> kafkaProducer;
    private final int flushIntervalMs;
    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.toGenericString());

    public KafkaProducerService(Properties properties) {
        this.kafkaProducer = new KafkaProducer<>(properties);
        this.flushIntervalMs = (Integer) properties.get("flush.interval.ms");
    }

    public void publish(String topic, Object object) {
        String message = JsonConverter.toJsonString(object).orElseThrow(RuntimeException::new);
        this.kafkaProducer.send(new ProducerRecord<>(topic, message));
    }

    public void flush() {
        this.kafkaProducer.flush();
    }

    @Override
    public void start() {
        LOGGER.info("Starting scheduled event publisher");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                kafkaProducer.flush();
            }
        }, 6000, flushIntervalMs);
        LOGGER.info("Scheduled event publisher started");
    }
}
