package com.grydtech.msstack.core.connectors.messagebus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartitionMetaData {
    private int partition;
    private long savedComittedOffset;
}
