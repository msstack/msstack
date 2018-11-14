package com.grydtech.msstack.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationProperties {

    private static final Properties prop;

    static {
        prop = new Properties();

        try {
            InputStream input = ConfigurationProperties.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(input);
        } catch (IOException ex) {
            System.out.println("Could not load config.properties");
            ex.printStackTrace();
        }
    }

    public static String get(ConfigKey key) {
        return prop.getProperty(key.toString());
    }
}
