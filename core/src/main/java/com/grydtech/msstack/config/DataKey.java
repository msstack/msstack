package com.grydtech.msstack.config;

public enum DataKey {

    DATA_ENTITY("data.entity"),
    DATA_ENTITY_ROOT("data.entity.root"),
    DATA_ENTITY_PARTITIONS("data.entity.partitions");

    private String value;

    DataKey(String s) {
        value = s;
    }

    @Override
    public String toString() {
        return value;
    }
}
