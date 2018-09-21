package com.grydtech.msstack.database.leveldb;

/**
 * Created by dileka on 8/26/18.
 */

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

public class LeveldbTest {
    
    // static variable single_instance of type Singleton
    private static LeveldbTest leveldbTest = null;
    
    private DB levelDBStore;
    
    // private constructor restricted to this class itself
    private LeveldbTest(String filename) throws IOException {
        Options options = new Options();
        levelDBStore = factory.open(new File(filename), options);
    }
    
    // static method to create instance of Singleton class
    public static LeveldbTest getInstance(String filename) throws IOException {
        if (leveldbTest == null)
            leveldbTest = new LeveldbTest(filename);
        
        return leveldbTest;
    }
    
    public void put(String key, String value) {
        levelDBStore.put(bytes(key), bytes(value));
    }
    
    public byte[] getValue(String key) {
        return levelDBStore.get(bytes(key));
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
        Snapshot snapshot = levelDBStore.getSnapshot();
        return snapshot;
    }
    
    public byte[] getValueFromSnapshot(Snapshot snapshot, String key) {
        return levelDBStore.get(bytes(key), new ReadOptions().snapshot(snapshot));
    }
    
}
