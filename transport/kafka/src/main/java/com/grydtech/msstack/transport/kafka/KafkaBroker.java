package com.grydtech.msstack.transport.kafka;

import com.grydtech.msstack.core.EventHandler;
import com.grydtech.msstack.core.MessageBroker;

public class KafkaBroker implements MessageBroker {

	private final KafkaSender kafkaSender;

	public KafkaBroker() {
		kafkaSender = new KafkaSender();
	}

	public void publish(String topic, String message) {
		kafkaSender.send(topic, message);
	}

	public void registerHandler(Class<? extends EventHandler> aClass) {
		throw new UnsupportedOperationException();
	}

	public void unregisterHandler(Class<? extends EventHandler> aClass) {
		throw new UnsupportedOperationException();
	}
}
