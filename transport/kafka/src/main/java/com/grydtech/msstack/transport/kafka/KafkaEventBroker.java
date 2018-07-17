package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.Event;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());

    private KafkaProducerService kafkaProducerService;
    private KafkaConsumerService kafkaConsumerService;

    public KafkaEventBroker() {
        this.kafkaProducerService = new KafkaProducerService(this.getApplicationProperties());
        this.kafkaConsumerService = new KafkaConsumerService(this.getApplicationProperties());
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
