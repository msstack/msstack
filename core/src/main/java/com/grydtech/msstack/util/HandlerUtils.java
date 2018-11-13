package com.grydtech.msstack.util;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.services.HandlerWrapper;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class HandlerUtils {

    private static final Logger LOGGER = Logger.getLogger(HandlerUtils.class.getName());

    public static String getTopic(Class<? extends Handler> handlerClass) {
        try {
            return handlerClass.getDeclaredMethod("get").getReturnType().getSimpleName();
        } catch (NoSuchMethodException e) {
            LOGGER.warning(String.format("Topic for %s not found", handlerClass.getSimpleName()));
            return null;
        }
    }

    public static HandlerWrapper getHandlerWrapper(Class<? extends Handler> handlerClass) {
        Method handleMethod = handlerClass.getDeclaredMethods()[0];
        Class<? extends Message> messageClass = (Class<? extends Message>) handleMethod.getParameterTypes()[0];
        Class<? extends Entity> entityClass = (Class<? extends Entity>) handleMethod.getParameterTypes()[1];

        String handlerId = entityClass.getSimpleName().toLowerCase() + "_" + messageClass.getSimpleName().toLowerCase();

        return new HandlerWrapper(handlerId, handlerClass, messageClass, entityClass);
    }
}
