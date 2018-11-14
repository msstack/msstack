package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.services.MessageConsumer;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.logging.LogMessage;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.util.JsonConverter;
import com.grydtech.msstack.util.MessageBusUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public final class KafkaMessageBusConnector extends MessageBusConnector {
    private static final Logger LOGGER = Logger.getLogger(KafkaMessageBusConnector.class.getName());
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        executorService.submit(() -> {
            String topic = message.getTopic();
            String key = message.getEntityId().toString();

            String messageName = MessageBusUtils.getMessageName(message.getClass());
            String messageString = JsonConverter.toJsonString(message).orElseThrow(RuntimeException::new);
            String metadataString = JsonConverter.toJsonString(metadata).orElseThrow(RuntimeException::new);

            String value = messageName + "::" + metadataString + "::" + messageString;

            this.kafkaProducerService.publish(topic, key, value);
        });
    }

    @Override
    public void push(LogMessage logMessage) {
        executorService.submit(() -> {
            String topic = ConfigurationProperties.get(ConfigKey.LOGGING_TOPIC);
            String key = UUID.randomUUID().toString();
            String value = JsonConverter.toJsonString(logMessage).orElseThrow(RuntimeException::new);

            this.kafkaProducerService.publish(topic, key, value);
        });
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
