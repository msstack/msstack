package com.grydtech.msstack.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JsonConverter {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    private JsonConverter() {
    }

    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getObject(String jsonString, Class<T> sendingClass) {
        try {
            return objectMapper.readValue(jsonString, sendingClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
