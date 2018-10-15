package com.grydtech.msstack.transport.kafka.util;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.core.types.messaging.Message;
import com.grydtech.msstack.util.JsonConverter;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InvokeHelper {

    private static final Logger LOGGER = Logger.getLogger(InvokeHelper.class.toGenericString());

    private InvokeHelper() {
    }

    public static void invokeHandleMethod(Class<? extends Handler> handlerClass, String message) {
        try {
            Method methodToInvoke = handlerClass.getDeclaredMethod("accept", Message.class);
            Class<?> eventParameter = methodToInvoke.getParameterTypes()[0];
            Event event = (Event) JsonConverter.getObject(message, eventParameter).orElse(null);
            new Thread(() -> {
                try {
                    handlerClass.newInstance().accept(event);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }).start();
        } catch (NoSuchMethodException e) {
            LOGGER.severe("Method to invoke not found");
        }
    }
}
