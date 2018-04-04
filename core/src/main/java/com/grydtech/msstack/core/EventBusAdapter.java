package com.grydtech.msstack.core;

public interface EventBusAdapter {

	void publish(Event event);

	void registerHandler(Class<? extends EventHandler> handlerClass);

	void unregisterHandler(Class<? extends EventHandler> handlerClass);
}
