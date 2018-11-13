package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.util.HandlerUtils;
import com.grydtech.msstack.util.JsonConverter;
import com.grydtech.msstack.util.MessageBusUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TopicMessagesConsumer implements MessageConsumer {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Class<? extends Event>> messageMap = new HashMap<>();
    private final Map<String, Set<HandlerWrapper>> handlersMap = new HashMap<>();

    private final String topic;

    public TopicMessagesConsumer(Class<? extends Entity> entityClass) {
        this.topic = MessageBusUtils.getTopicByEntityClass(entityClass);
    }

    @Override
    public void registerEvent(Class<? extends Event> event) {
        if (Objects.equals(MessageBusUtils.getTopicByMessageClass(event), topic)) return;
        messageMap.put(MessageBusUtils.getMessageName(event), event);
    }

    @Override
    public void registerHandler(Class<? extends Handler> handler) {
        HandlerWrapper handlerWrapper = HandlerUtils.getHandlerWrapper(handler);
        if (!MessageBusUtils.getTopicByEntityClass(handlerWrapper.getEntityClass()).equals(topic)) return;

        if (handlersMap.containsKey(handlerWrapper.getKey())) {
            handlersMap.get(handlerWrapper.getKey()).add(handlerWrapper);
        } else {
            handlersMap.put(handlerWrapper.getKey(), Collections.singleton(handlerWrapper));
        }
    }

    @Override
    public void accept(String s) {
        String[] parts = s.split("::");
        Class<? extends Message> messageClass = messageMap.get(parts[0]);
        Set<HandlerWrapper> handlerWrappers = handlersMap.get(parts[0]);

        if (messageClass == null) return;

        Message message = JsonConverter.getObject(parts[2], messageClass).orElseThrow(RuntimeException::new);
        Map metadata = JsonConverter.getMap(parts[1]).orElseThrow(RuntimeException::new);

        String messageType = (String) metadata.get("type");

        final Entity entity;

        synchronized (this) {
            entity = (Entity) SnapshotConnector.getInstance().get(message.getEntityId().toString(), message.getEntityClass());

            if (messageType.equals("EVENT")) {
                entity.apply((Event) message);
                SnapshotConnector.getInstance().put(entity.getId().toString(), entity);
            }
        }

        if (handlerWrappers == null) return;

        UUID flowId = (UUID) metadata.get("flowId");

        handlerWrappers.forEach(hw -> {
            executorService.submit(() -> {
                try {
                    Handler handler = hw.getHandlerClass().newInstance();
                    handler.handle(message, metadata, flowId, entity);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
