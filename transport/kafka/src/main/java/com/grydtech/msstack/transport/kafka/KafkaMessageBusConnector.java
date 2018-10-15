package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.util.JsonConverter;

import java.io.IOException;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public class KafkaMessageBusConnector extends MessageBusConnector {

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageBusConnector.class.toGenericString());

    private KafkaProducerService kafkaProducerService;
    private KafkaConsumerService kafkaConsumerService;

    public KafkaMessageBusConnector() {
        this.kafkaProducerService = new KafkaProducerService(applicationConfiguration);
        this.kafkaConsumerService = new KafkaConsumerService(applicationConfiguration);
    }

    @Override
    public void push(Message message) {
        String topic = message.getTopic();
        String eventName = message.getClass().getSimpleName();
        String eventData = JsonConverter.toJsonString(message).orElseThrow(RuntimeException::new);
        this.kafkaProducerService.publish(topic, eventName, eventData);
    }

    @Override
    public void connect() throws IOException {
        LOGGER.info("Starting KafkaMessageBusConnector");
        this.kafkaConsumerService.setTopicClassMap(getTopicClassSetMap());
        this.kafkaProducerService.start();
        this.kafkaConsumerService.start();
        LOGGER.info("KafkaMessageBusConnector Started");
    }

    @Override
    public void disconnect() throws IOException {
        this.kafkaProducerService.flush();
    }
}
