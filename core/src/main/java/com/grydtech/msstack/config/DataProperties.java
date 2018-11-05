package com.grydtech.msstack.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DataProperties {

    private static final Properties prop;

    static {
        prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("data.properties");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(DataKey key) {
        return prop.getProperty(key.toString());
    }
}
