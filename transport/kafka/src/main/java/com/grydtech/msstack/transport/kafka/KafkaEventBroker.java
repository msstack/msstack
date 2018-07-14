package com.grydtech.msstack.transport.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.annotation.Event;
import com.grydtech.msstack.core.annotation.ServerComponent;
import com.grydtech.msstack.core.component.EventBroker;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.util.JsonConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


@ServerComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());
    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;

    @Override
    public void publish(BasicEvent event) {
        Event eventAnnotation = event.getClass().getAnnotation(Event.class);
        Map<String, Object> object = new HashMap<>();
        object.put("event", event.getClass().getSimpleName());
        object.put("data", event);
        String message = JsonConverter.toJsonString(object).orElseThrow(RuntimeException::new);
        this.producer.send(new ProducerRecord<>(eventAnnotation.stream(), message));
    }

    @Override
    public void flush() {
        this.producer.flush();
    }

    @Override
    public void start() {
        LOGGER.info("Starting KafkaEventBroker");
        this.producer = new KafkaProducer<>(Config.PRODUCER_PROPERTIES);
        this.consumer = new KafkaConsumer<>(Config.CONSUMER_PROPERTIES);
        consumer.subscribe(getStreams());

        LOGGER.info("Starting scheduled consumer");
        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    JsonNode jsonObject = JsonConverter.getJsonNode(record.value()).orElseThrow(RuntimeException::new);
                    String key = record.topic() + "::" + jsonObject.get("event").asText();

                    if (getHandlers().containsKey(key)) {
                        Class<? extends EventHandler> handlerClass = getHandlers().get(key);
                        try {
                            Method handleMethod = handlerClass.getMethod("handle", BasicEvent.class);
                            Class<?> eventParameter = handleMethod.getParameterTypes()[0];
                            JsonNode eventData = jsonObject.get("data");
                            Object o = JsonConverter.nodeToObject(eventData, eventParameter);
                            handlerClass.newInstance().handle((BasicEvent)o);
                        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                }
            }
        });

        LOGGER.info("Starting scheduled EventBroker dispatcher");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                producer.flush();
            }
        }, 1000);

        LOGGER.info("KafkaEventBroker Started");
    }
}
