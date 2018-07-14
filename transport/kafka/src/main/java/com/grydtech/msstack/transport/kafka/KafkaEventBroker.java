package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.Event;
import com.grydtech.msstack.core.annotation.ServerComponent;
import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.transport.kafka.util.ConfigHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@ServerComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());
    private KafkaProducerService kafkaProducerService;
    private KafkaConsumerService kafkaConsumerService;

    public KafkaEventBroker() {
        this.kafkaProducerService = new KafkaProducerService(ConfigHelper.PRODUCER_PROPERTIES);
        this.kafkaConsumerService = new KafkaConsumerService(ConfigHelper.CONSUMER_PROPERTIES);
    }

    @Override
    public void publish(BasicEvent event) {
        Event eventAnnotation = event.getClass().getAnnotation(Event.class);
        Map<String, Object> object = new HashMap<>();
        object.put("event", event.getClass().getSimpleName());
        object.put("data", event);
        this.kafkaProducerService.publish(eventAnnotation.stream(), object);
    }

    @Override
    public void flush() {
        this.kafkaProducerService.flush();
    }

    @Override
    public void start() {
        LOGGER.info("Starting KafkaEventBroker");
        this.kafkaConsumerService.setHandlerClassMap(getHandlers());
        this.kafkaConsumerService.setStreams(getStreams());
        this.kafkaProducerService.start();
        this.kafkaConsumerService.start();
        LOGGER.info("KafkaEventBroker Started");
    }
}
