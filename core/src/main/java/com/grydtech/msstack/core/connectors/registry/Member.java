package com.grydtech.msstack.core.connectors.registry;

import com.grydtech.msstack.config.DataKey;
import com.grydtech.msstack.config.DataProperties;
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
        instance.setName(DataProperties.get(DataKey.DATA_ENTITY).toLowerCase() + "_service");
    }

    private String id;
    private String name;
    private String host;
    private int port;
}
