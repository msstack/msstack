package com.grydtech.msstack.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class YamlConverter {

    private static final Logger LOGGER = Logger.getLogger(YamlConverter.class.toGenericString());
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    private YamlConverter() {
    }

    public static <T> Optional<T> getObject(File file, Class<T> sendingClass) {
        try {
            return Optional.of(objectMapper.readValue(file, sendingClass));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }
}
