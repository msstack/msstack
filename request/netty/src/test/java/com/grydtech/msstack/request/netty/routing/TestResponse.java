package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.types.messaging.Response;

import java.util.UUID;

public class TestResponse extends Response {

    private UUID id;
    private String payload;

    public TestResponse() {
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Response setId(UUID id) {
        this.id = id;
        return this;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public TestResponse setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestResponse that = (TestResponse) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return payload != null ? payload.equals(that.payload) : that.payload == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}
