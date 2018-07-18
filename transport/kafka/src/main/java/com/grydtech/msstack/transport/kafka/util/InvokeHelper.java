package com.grydtech.msstack.transport.kafka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.handler.EventHandler;
import com.grydtech.msstack.util.JsonConverter;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InvokeHelper {

    private static final Logger LOGGER = Logger.getLogger(InvokeHelper.class.toGenericString());

    private InvokeHelper() {
    }

    public static void invokeHandleMethod(Class<? extends EventHandler> handlerClass, String message) {
        Method handleMethod = handlerClass.getDeclaredMethods()[0];
        Class<?> eventParameter = handleMethod.getParameterTypes()[0];
        BasicEvent event = (BasicEvent) JsonConverter.getObject(message, eventParameter).orElse(null);
        new Thread(() -> {
            try {
                handlerClass.newInstance().handle(event);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }).start();
    }
}
