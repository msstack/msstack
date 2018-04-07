package com.grydtech.msstack.core;

public interface MessageBroker {

	void publish(String topic, String message);

	void registerHandler(Class<? extends EventHandler> handlerClass);

	void unregisterHandler(Class<? extends EventHandler> handlerClass);
}
