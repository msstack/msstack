package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.util.HandlerUtils;
import com.grydtech.msstack.util.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public final class KafkaMessageBusConnector extends MessageBusConnector {

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageBusConnector.class.getName());

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    private final HashMap<String, Set<Handler>> consumers = new HashMap<>();

    public KafkaMessageBusConnector() {
        this.kafkaProducerService = new KafkaProducerService();
        this.kafkaConsumerService = new KafkaConsumerService();
    }

    public final void attach(Handler handler) {
        final String topic = HandlerUtils.getTopic(handler.getClass());
        Set<Handler> consumersForTopic = consumers.getOrDefault(topic, new HashSet<>());
        consumersForTopic.add(handler);
        consumers.put(topic, consumersForTopic);
    }

    public final void detach(Handler handler) {
        final String topic = HandlerUtils.getTopic(handler.getClass());
        if (topic != null) {
            this.consumers.get(topic).removeIf(h -> h.equals(handler));
            LOGGER.info(String.format("[TOPIC][%s] -> %s | detached", topic, handler.toString()));
        } else {
            LOGGER.warning(String.format("[TOPIC][%s] -> %s | not detached", null, handler.toString()));
        }
    }

    @Override
    public void push(Message message) {
        String topic = message.getTopic();
        String eventName = message.getClass().getSimpleName();
        String eventData = JsonConverter.toJsonString(message.getPayload()).orElseThrow(RuntimeException::new);
        this.kafkaProducerService.publish(topic, 0, eventName, eventData);
    }

    @Override
    public void connect() throws IOException {
        LOGGER.info("Starting KafkaMessageBusConnector");
        this.kafkaConsumerService.setConsumers(this.consumers);
        this.kafkaProducerService.start();
        this.kafkaConsumerService.start();
        LOGGER.info("KafkaMessageBusConnector Started");
    }

    @Override
    public void disconnect() throws IOException {
        this.kafkaProducerService.flush();
    }
}
