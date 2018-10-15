package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.types.Entity;

public class TestEntity implements Entity<String> {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Entity<String> setId(String id) {
        this.id = id;
        return this;
    }
}
