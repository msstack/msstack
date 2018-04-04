package com.grydtech.msstack.core;

import java.util.ArrayList;
import java.util.Arrays;

public class MicroserviceRunner {

	private final ArrayList<Class<? extends Handler>> handlers;

	private String host;

	private int port;

	protected MicroserviceRunner() {
		handlers = new ArrayList<>();
	}

	@SafeVarargs
	public final MicroserviceRunner registerHandlers(Class<? extends Handler>... handlers) {
		this.handlers.addAll(Arrays.asList(handlers));
		return this;
	}

	public MicroserviceRunner unregisterHandler(Class<? extends Handler> handler) {
		this.handlers.remove(handler);
		return this;
	}

	public MicroserviceRunner bind(String host, int port) {
		this.host = host;
		this.port = port;
		return this;
	}

	public void run() {
		// Register all handlers and start the runner
		handlers.forEach(handler -> {
			try {
				if (handler.isAssignableFrom(EventHandler.class)) {
					EventBusAdapter.class.newInstance().registerHandler(handler.asSubclass(EventHandler.class));
				}
			}
			catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
