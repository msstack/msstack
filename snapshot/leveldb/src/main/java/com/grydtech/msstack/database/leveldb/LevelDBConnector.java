package com.grydtech.msstack.database.leveldb;

/**
 * Created by dileka on 8/26/18.
 */

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.core.connectors.snapshot.SnapshotConnector;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public final class LevelDBConnector extends SnapshotConnector<String, String> {

    private static DB levelDBStore = null;

    private Snapshot snapshot;

    @Override
    public void put(String key, String value) {
        levelDBStore.put(bytes(key), bytes(value));
    }

    @Override
    public void put(Map<String, String> data) throws IOException {
        try (WriteBatch batch = levelDBStore.createWriteBatch()) {
            data.forEach((k, v) -> batch.put(bytes(k), bytes(v)));
            levelDBStore.write(batch);
        }
    }

    @Override
    public String get(String key) {
        return new String(levelDBStore.get(bytes(key)));
    }

    @Override
    public String getFromSnapshot(String key) {
        return asString(levelDBStore.get(bytes(key), new ReadOptions().snapshot(this.snapshot)));
    }

    @Override
    public void delete(String key) {
        levelDBStore.delete(bytes(key));
    }

    @Override
    public void updateSnapshot() {
        this.snapshot = levelDBStore.getSnapshot();
    }

    @Override
    public void connect() throws IOException {
        String dbName = ConfigurationProperties.get(ConfigKey.SNAPSHOT_NAME);
        Options options = new Options();
        levelDBStore = factory.open(new File(dbName), options);
    }

    @Override
    public void disconnect() throws IOException {
        levelDBStore.close();
    }
}
