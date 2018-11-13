package com.grydtech.msstack.util;

import com.grydtech.msstack.core.types.Entity;

import java.util.logging.Logger;

public class EntityUtils {

    private static final Logger LOGGER = Logger.getLogger(EntityUtils.class.getName());

    public static String getTopic(Class<? extends Entity> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }
}
