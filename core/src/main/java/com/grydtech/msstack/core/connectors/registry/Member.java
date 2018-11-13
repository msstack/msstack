package com.grydtech.msstack.core.connectors.registry;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import lombok.Data;

import java.util.UUID;

/**
 * This class contains all properties of the running service that helps to uniquely identify it
 */
@Data
public class Member {

    private static final Member instance;

    static {
        instance = new Member();
        instance.setId(UUID.randomUUID().toString());
        instance.setName(ConfigurationProperties.get(ConfigKey.SERVICE_NAME).toLowerCase());
    }

    private String id;
    private String name;
    private String host;
    private int port;
}
