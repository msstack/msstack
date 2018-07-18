package com.grydtech.msstack.transport.kafka.services;

import com.grydtech.msstack.core.configuration.ApplicationConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class KafkaProducerService extends KafkaService {

    private final KafkaProducer<String, String> kafkaProducer;
    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.toGenericString());

    public KafkaProducerService(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfiguration.getBroker().getBootstrap());
        properties.put(ProducerConfig.ACKS_CONFIG, applicationConfiguration.getBroker().getAcks());
        properties.put(ProducerConfig.RETRIES_CONFIG, applicationConfiguration.getBroker().getRetries());

        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    public void publish(String topic, String eventName, String eventData) {
        this.kafkaProducer.send(new ProducerRecord<>(topic, eventName, eventData));
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
        }, 6000, applicationConfiguration.getBroker().getInterval());
        LOGGER.info("Scheduled event publisher started");
    }
}
