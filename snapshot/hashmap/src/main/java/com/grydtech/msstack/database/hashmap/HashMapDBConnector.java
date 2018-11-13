package com.grydtech.msstack.database.hashmap;

import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.util.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public final class HashMapDBConnector extends SnapshotConnector {

    private static final Logger LOGGER = Logger.getLogger(HashMapDBConnector.class.getName());

    private static Map<String, String> db;

    @Override
    public void put(String key, Object value) {
        Optional<String> jsonString = JsonConverter.toJsonString(value);
        jsonString.ifPresent(s -> db.put(key, jsonString.get()));
    }

    @Override
    public <T> T get(String key, Class<T> outputClass) {
        try {
            if (db.containsKey(key)) {
                return JsonConverter.getObject(db.get(key), outputClass).orElse(null);
            } else {
                return outputClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String key) {
        db.remove(key);
    }

    @Override
    public void connect() throws IOException {
        if (db != null) db.clear();
        db = new HashMap<>();
        LOGGER.info("HashMap db created");
    }

    @Override
    public void disconnect() throws IOException {
        if (db != null) db.clear();
        db = null;
    }
}
