package com.grydtech.msstack.util;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

public class MessageBusUtils {

    public static String getTopicByEntityClass(Class<? extends Entity> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

    public static String getTopicByMessageClass(Class<? extends Message> messageClass) {
        try {
            return messageClass.getDeclaredMethod("getEntityClass").getReturnType().getSimpleName().toLowerCase();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMessageName(Class<? extends Message> messageClass) {
        try {
            return messageClass.getDeclaredMethod("getEntityClass").getReturnType().getSimpleName().toLowerCase() +
                    "::" + messageClass.getSimpleName().toLowerCase();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
