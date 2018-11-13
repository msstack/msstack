package com.grydtech.msstack.util;

import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;

public class MessageUtils {

    public static String getMessageName(Class<? extends Message> messageClass) {
        try {
            Class<? extends Entity> entityClass = (Class<? extends Entity>) messageClass.getDeclaredMethod("getEntityClass").getReturnType();
            return entityClass.getSimpleName().toLowerCase() + "_" + messageClass.getSimpleName().toLowerCase();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
