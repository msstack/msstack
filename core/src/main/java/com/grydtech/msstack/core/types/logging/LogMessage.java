package com.grydtech.msstack.core.types.logging;

import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import lombok.Data;

import java.util.Date;

@Data
public class LogMessage {
    private final String serviceGroup = ConfigurationProperties.get(ConfigKey.SERVICE_NAME);
    private final String serviceId = ConfigurationProperties.get(ConfigKey.SERVICE_ID);
    private final String level;
    private final Date date = new Date();
    private final String message;
    private final String className;
}
