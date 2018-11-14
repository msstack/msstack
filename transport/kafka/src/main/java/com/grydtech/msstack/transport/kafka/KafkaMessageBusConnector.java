package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.services.MessageConsumer;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.util.JsonConverter;
import com.grydtech.msstack.util.MessageBusUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public final class KafkaMessageBusConnector extends MessageBusConnector {

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageBusConnector.class.getName());

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    private final Map<String, MessageConsumer> consumers = new HashMap<>();

    public KafkaMessageBusConnector() {
        this.kafkaProducerService = new KafkaProducerService();
        this.kafkaConsumerService = new KafkaConsumerService();
    }

    public final void attach(Class<? extends Entity> entityClass, MessageConsumer consumer) {
        consumers.put(MessageBusUtils.getTopicByEntityClass(entityClass), consumer);
        LOGGER.info(String.format("[TOPIC][%s] -> %s | attached", MessageBusUtils.getTopicByEntityClass(entityClass), consumer.getClass().getSimpleName()));
    }

    public final void detach(Class<? extends Entity> entityClass) {
        consumers.remove(MessageBusUtils.getTopicByEntityClass(entityClass));
        LOGGER.info(String.format("[TOPIC][%s] | detached", MessageBusUtils.getTopicByEntityClass(entityClass)));
    }

    @Override
    public void push(Message message, Map metadata) {
        String topic = message.getTopic();
        String eventName = MessageBusUtils.getMessageName(message.getClass());
        String messageString = JsonConverter.toJsonString(message).orElseThrow(RuntimeException::new);
        String metadataString = JsonConverter.toJsonString(metadata).orElseThrow(RuntimeException::new);
        this.kafkaProducerService.publish(topic, message.getEntityId().toString(), eventName, metadataString, messageString);
    }

    @Override
    public void connect() {
        LOGGER.info("Starting Kafka Connection");
        this.kafkaConsumerService.setConsumers(this.consumers);
        this.kafkaProducerService.start();
        this.kafkaConsumerService.start();
        LOGGER.info("Kafka Connected");
    }

    @Override
    public void disconnect() {
        this.kafkaProducerService.flush();
    }
}
