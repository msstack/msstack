package com.grydtech.msstack.sample;

import com.grydtech.msstack.core.types.Entity;
import lombok.Data;

import java.util.UUID;

@Data
public final class SampleEntity implements Entity<UUID> {

    private UUID id;
    private SampleRootEntity rootEntity;
}
