package com.grydtech.msstack.util;

import com.grydtech.msstack.core.handler.Handler;

import java.util.logging.Logger;

public class HandlerUtils {

    private static final Logger LOGGER = Logger.getLogger(HandlerUtils.class.toGenericString());

    public static String getTopic(Class<? extends Handler> handlerClass) {
        try {
            return handlerClass.getDeclaredMethod("get").getReturnType().getSimpleName();
        } catch (NoSuchMethodException e) {
            LOGGER.warning(String.format("Topic for %s not found", handlerClass.getSimpleName()));
            return null;
        }
    }
}
