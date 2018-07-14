package com.grydtech.msstack.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JsonConverter {

    private static final Logger LOGGER = Logger.getLogger(JsonConverter.class.toGenericString());
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    private JsonConverter() {
    }

    public static Optional<String> toJsonString(Object object) {
        try {
            return Optional.of(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static <T> Optional<T> getObject(String jsonString, Class<T> sendingClass) {
        try {
            return Optional.of(objectMapper.readValue(jsonString, sendingClass));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static Optional<JsonNode> getJsonNode(String jsonString) {
        try {
            return Optional.of(objectMapper.readTree(jsonString));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static <T> Optional<T> nodeToObject(JsonNode jsonNode, Class<T> sendingClass) {
        try {
            return Optional.of(objectMapper.treeToValue(jsonNode, sendingClass));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }
}
