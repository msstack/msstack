package com.grydtech.msstack.database.leveldb;

/**
 * Created by dileka on 8/26/18.
 */

import com.grydtech.msstack.core.connectors.database.DatabaseConnector;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class LeveldbConnector extends DatabaseConnector<String> {

    private static DB levelDBStore = null;
    private LeveldbConnector leveldbConnector = null;

    public LeveldbConnector() {
    }

    @Override
    public void putKey(String key, String value) {
        levelDBStore.put(bytes(key), bytes(value));
    }

    @Override
    public String getValue(String key) {

        return new String(levelDBStore.get(bytes(key)));
    }

    public void deleteKey(String key) {
        levelDBStore.delete(bytes(key));
    }

    public void putInBatch(List<String> keys, List<String> values) throws IOException {
        WriteBatch batch = levelDBStore.createWriteBatch();
        for (int i = 0; i < keys.size(); i++) {
            batch.put(bytes(keys.get(i)), bytes(values.get(i)));
        }

        levelDBStore.write(batch);
        batch.close();
    }

    public Snapshot createSnapshot() {
        return levelDBStore.getSnapshot();
    }

    public byte[] getValueFromSnapshot(Snapshot snapshot, String key) {
        return levelDBStore.get(bytes(key), new ReadOptions().snapshot(snapshot));
    }

    @Override
    public void connect() throws IOException {
        Options options = new Options();
        levelDBStore = factory.open(new File(applicationConfiguration.getDatabaseConfiguration().getDatabaseName()), options);
    }

    @Override
    public void disconnect() throws IOException {
        levelDBStore.close();
    }
}
