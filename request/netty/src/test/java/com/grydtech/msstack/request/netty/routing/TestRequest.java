package com.grydtech.msstack.request.netty.routing;

public class TestRequest {

    private String id;
    private String data;

    public String getId() {
        return id;
    }

    public TestRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return data;
    }

    public TestRequest setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestRequest that = (TestRequest) o;

        if (!id.equals(that.id)) return false;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }
}
