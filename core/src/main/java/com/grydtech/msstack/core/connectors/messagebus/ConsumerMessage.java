package com.grydtech.msstack.core.connectors.messagebus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsumerMessage {
    private String key;
    private String value;
    private int partition;
    private long offset;
}
