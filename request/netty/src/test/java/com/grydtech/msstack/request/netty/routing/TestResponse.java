package com.grydtech.msstack.request.netty.routing;

public class TestResponse {

    private String result;

    public TestResponse setResult(String result) {
        this.result = result;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestResponse that = (TestResponse) o;

        return result.equals(that.result);
    }

    @Override
    public int hashCode() {
        return result.hashCode();
    }
}
