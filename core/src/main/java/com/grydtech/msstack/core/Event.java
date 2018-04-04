package com.grydtech.msstack.core;

import com.grydtech.msstack.core.annotations.EventBus;

public abstract class Event {

	public void emit() {
		try {
			EventBusAdapter eventBus = this.getClass().getAnnotation(EventBus.class).value().newInstance();
			eventBus.publish(this);
		}
		catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
	}
}
