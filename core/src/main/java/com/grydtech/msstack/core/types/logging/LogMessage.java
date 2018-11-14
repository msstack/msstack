package com.grydtech.msstack.core.types.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import lombok.Data;

@Data
public class LogMessage {
    private final String name = ConfigurationProperties.get(ConfigKey.SERVICE_ID);
    private final String level;
    private final String message;
    @JsonProperty("class")
    private final String className;
}
