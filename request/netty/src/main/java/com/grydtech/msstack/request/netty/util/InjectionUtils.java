package com.grydtech.msstack.request.netty.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public final class InjectionUtils {

    private InjectionUtils() {

    }

    /**
     * InjectInstance parameters to object, and return it
     *
     * @param object     Object to which to injectParameters
     * @param parameters Parameters to injectParameters
     */
    public static void injectParameters(Object object, Map<String, ?> parameters) {
        Class<?> objectClass = object.getClass();
        parameters.forEach((key, value) -> {
            try {
                Field declaredField = objectClass.getDeclaredField(key);
                boolean isFieldAccessible = declaredField.isAccessible();
                if (!isFieldAccessible) {
                    declaredField.setAccessible(true);
                }
                if (value instanceof String) {
                    declaredField.set(object, value);
                } else if (value instanceof List) {
                    List valueList = (List) value;
                    if (valueList.size() == 1) {
                        declaredField.set(object, valueList.get(0));
                    } else {
                        declaredField.set(object, value);
                    }
                }
                declaredField.setAccessible(isFieldAccessible);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
    }
}
