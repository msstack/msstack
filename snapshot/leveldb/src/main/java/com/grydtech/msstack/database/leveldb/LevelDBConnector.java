package com.grydtech.msstack.database.leveldb;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import com.grydtech.msstack.util.JsonConverter;
import org.iq80.leveldb.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public final class LevelDBConnector extends SnapshotConnector {


    private static final Logger LOGGER = Logger.getLogger(LevelDBConnector.class.getName());

    private static DB db;

    private Snapshot snapshot;

    @Override
    public void put(String key, Object value) {
        Optional<String> jsonString = JsonConverter.toJsonString(value);
        jsonString.ifPresent(s -> db.put(bytes(key), bytes(s)));
    }

    @Override
    public void put(Map<String, Object> data) throws IOException {
        try (WriteBatch batch = db.createWriteBatch()) {
            data.forEach((k, v) -> {
                Optional<String> jsonString = JsonConverter.toJsonString(v);
                jsonString.ifPresent(s -> batch.put(bytes(k), bytes(s)));
            });
            db.write(batch);
        }
    }

    @Override
    public <T> T get(String key, Class<T> outputClass) {
        String value = null;
        try {
            value = asString(db.get(bytes(key)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (value == null) {
                return outputClass.newInstance();
            } else {
                return JsonConverter.getObject(value, outputClass).orElse(outputClass.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T getFromSnapshot(String key, Class<T> outputClass) {
        return JsonConverter.getObject(asString(db.get(bytes(key), new ReadOptions().snapshot(this.snapshot))), outputClass).get();
    }

    @Override
    public void delete(String key) {
        db.delete(bytes(key));
    }

    @Override
    public void updateSnapshot() {
        this.snapshot = db.getSnapshot();
    }

    @Override
    public void connect() throws IOException {
        String dbName = ConfigurationProperties.get(ConfigKey.SNAPSHOT_NAME);
        Options options = new Options();
        db = factory.open(new File(dbName), options);
        LOGGER.info("LevelDB Connected");
    }

    @Override
    public void disconnect() throws IOException {
        db.close();
    }
}