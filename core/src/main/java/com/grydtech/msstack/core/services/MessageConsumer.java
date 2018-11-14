package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.connectors.messagebus.ConsumerMessage;
import com.grydtech.msstack.core.connectors.messagebus.PartitionMetaData;
import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Message;

import java.util.Collection;
import java.util.function.Consumer;

public interface MessageConsumer extends Consumer<ConsumerMessage> {

    void registerMessage(Class<? extends Message> message);

    void registerHandler(Class<? extends Handler> handler);

    void setNextOffsetsToProcess(Collection<PartitionMetaData> partitionMetaDataCollection);
}
