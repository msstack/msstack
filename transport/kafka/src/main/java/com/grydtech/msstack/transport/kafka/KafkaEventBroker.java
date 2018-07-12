package com.grydtech.msstack.transport.kafka;

import com.google.common.base.CaseFormat;
import com.grydtech.msstack.core.Event;
import com.grydtech.msstack.core.annotation.ServerComponent;
import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.util.JsonConverter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;


@ServerComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());
    private final Producer<String, String> producer;

    public KafkaEventBroker() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void publish(Event event) {
        String topic = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, event.getClass().getSimpleName());
        String message = JsonConverter.toJsonString(event).orElseThrow(RuntimeException::new);
        this.producer.send(new ProducerRecord<>(topic, message));
        this.producer.flush();
    }

    @Override
    public void subscribe(Class<? extends EventHandler> handlerClass) {
        LOGGER.info(String.format("Subscribed to KafkaEventBroker -%s", handlerClass.getName()));
    }

    @Override
    public void subscribeAll(Set<Class<? extends EventHandler>> subscriberSet) {
        LOGGER.info(String.format("Subscribed to KafkaEventBroker -%s", subscriberSet.toString()));
    }

    @Override
    public void unsubscribe(Class<? extends EventHandler> handlerClass) {
        LOGGER.info(String.format("Unsubscribed from KafkaEventBroker -%s", handlerClass.getName()));
    }

    @Override
    public void start() {
        LOGGER.info("KafkaEventBroker Started");
    }
}
