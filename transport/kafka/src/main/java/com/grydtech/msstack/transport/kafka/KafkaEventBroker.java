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
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;


@ServerComponent
public class KafkaEventBroker extends EventBroker {

    private static final Logger LOGGER = Logger.getLogger(KafkaEventBroker.class.toGenericString());
    private final Producer<String, String> producer;
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();


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

        LOGGER.info("Starting scheduled EventBroker dispatcher");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("Checking for tasks in broker queue...");
                Runnable task = taskQueue.poll();
                while (task != null) {
                    task.run();
                    task = taskQueue.poll();
                }
                producer.flush();
            }
        }, 6000);
    }

    @Override
    public void publish(Event event) {
        String topic = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, event.getClass().getName());
        String message = JsonConverter.toJsonString(event).orElseThrow(RuntimeException::new);
        Runnable sendTask = () -> this.producer.send(new ProducerRecord<>(topic, message));
        taskQueue.add(sendTask);
    }

    @Override
    public void flush() {
        this.producer.flush();
    }

    @Override
    public void subscribe(Class<? extends EventHandler> handlerClass) {
        LOGGER.info(String.format("Subscribed to KafkaEventBroker -%s", handlerClass.getSimpleName()));
    }

    @Override
    public void subscribeAll(Set<Class<? extends EventHandler>> subscriberSet) {
        LOGGER.info(String.format("Subscribed to KafkaEventBroker -%s", subscriberSet.toString()));
    }

    @Override
    public void unsubscribe(Class<? extends EventHandler> handlerClass) {
        LOGGER.info(String.format("Unsubscribed from KafkaEventBroker -%s", handlerClass.getSimpleName()));
    }

    @Override
    public void start() {
        LOGGER.info("KafkaEventBroker Started");
    }
}
