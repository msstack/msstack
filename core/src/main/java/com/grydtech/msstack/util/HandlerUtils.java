package com.grydtech.msstack.util;

import com.grydtech.msstack.core.handler.Handler;

import java.lang.reflect.InvocationTargetException;
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

    public static <T extends Handler> T instantiate(Class<T> handlerClass) {
        try {
            return handlerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Cannot instantiate handler - " + handlerClass.getName());
            return null;
        } catch (NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Constructor not found for handler - " + handlerClass.getName());
            return null;
        }
    }
}
