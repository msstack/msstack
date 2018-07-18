package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.Event;
import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.transport.kafka.services.KafkaConsumerService;
import com.grydtech.msstack.transport.kafka.services.KafkaProducerService;
import com.grydtech.msstack.util.JsonConverter;

import java.util.logging.Logger;

@SuppressWarnings("unused")
@FrameworkComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());

    private KafkaProducerService kafkaProducerService;
    private KafkaConsumerService kafkaConsumerService;

    public KafkaEventBroker() {
        this.kafkaProducerService = new KafkaProducerService(this.getApplicationConfiguration());
        this.kafkaConsumerService = new KafkaConsumerService(this.getApplicationConfiguration());
    }

    @Override
    public void publish(BasicEvent event) {
        String eventStream = event.getClass().getAnnotation(Event.class).stream();
        String eventName = event.getClass().getSimpleName();
        String eventData = JsonConverter.toJsonString(event).orElseThrow(RuntimeException::new);
        this.kafkaProducerService.publish(eventStream, eventName, eventData);
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
