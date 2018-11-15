package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.connectors.messagebus.ConsumerMessage;
import com.grydtech.msstack.core.connectors.messagebus.PartitionMetaData;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.util.HandlerUtils;
import com.grydtech.msstack.util.JsonConverter;
import com.grydtech.msstack.util.MessageBusUtils;

import java.util.*;

public class TopicMessagesConsumer implements MessageConsumer {
    private final Map<String, Class<? extends Message>> messageMap = new HashMap<>();
    private final Map<String, Set<HandlerWrapper>> handlersMap = new HashMap<>();
    private final Map<Integer, Long> processedMessages = new HashMap<>();
    private final Map<Integer, Long> appliedMessages = new HashMap<>();

    private final String topic;

    public TopicMessagesConsumer(Class<? extends Entity> entityClass) {
        this.topic = MessageBusUtils.getTopicByEntityClass(entityClass);
    }

    @Override
    public void registerMessage(Class<? extends Message> message) {
        if (!MessageBusUtils.getTopicByMessageClass(message).equals(this.topic)) return;
        messageMap.put(MessageBusUtils.getMessageName(message), message);
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
    public void assignPartition(PartitionMetaData partitionMetaData) {
        processedMessages.put(partitionMetaData.getPartition(), partitionMetaData.getSavedComittedOffset());
    }

    @Override
    public void invokePartition(PartitionMetaData partitionMetaData) {
        processedMessages.remove(partitionMetaData.getPartition());
    }

    @Override
    public void accept(ConsumerMessage consumerMessage) {
        String[] parts = consumerMessage.getValue().split("::");
        Class<? extends Message> messageClass = messageMap.get(parts[0]);
        Set<HandlerWrapper> handlerWrappers = handlersMap.get(parts[0]);

        if (messageClass == null) return;

        Message message = JsonConverter.getObject(parts[2], messageClass).orElse(null);
        Map metadata = JsonConverter.getMap(parts[1]).orElse(null);

        if (metadata == null || message == null) return;

        String messageType = (String) metadata.get("type");

        synchronized (message.getEntityId().toString().intern()) {
            final Entity entity;

            entity = (Entity) SnapshotConnector.getInstance().get(message.getEntityId().toString(), message.getEntityClass());

            if (entity == null) return;

            // Remove duplicate messages
            if (appliedMessages.get(consumerMessage.getPartition()) != null &&
                    consumerMessage.getOffset() <= appliedMessages.get(consumerMessage.getPartition())) return;

            if (messageType.equals("EVENT")) {
                entity.applyEventAndIncrementVersion((Event) message);
                SnapshotConnector.getInstance().put(entity.getEntityId().toString(), entity);
                appliedMessages.put(consumerMessage.getPartition(), consumerMessage.getOffset());
            }

            // If offset previously handled by another service
            if (processedMessages.get(consumerMessage.getPartition()) != null &&
                    consumerMessage.getOffset() < processedMessages.get(consumerMessage.getPartition())) return;

            if (handlerWrappers == null) return;

            UUID flowId = UUID.fromString((String) metadata.get("flowId"));

            handlerWrappers.forEach(hw -> {
                try {
                    Handler handler = hw.getHandlerClass().newInstance();
                    handler.handle(message, metadata, flowId, entity);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            processedMessages.put(consumerMessage.getPartition(), consumerMessage.getOffset());
        }
    }
}
