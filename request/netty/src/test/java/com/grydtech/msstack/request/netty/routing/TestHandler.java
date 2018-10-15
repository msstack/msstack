package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.handler.CommandHandler;
import com.grydtech.msstack.core.types.QueryArgs;

import java.util.function.Consumer;

public class TestHandler implements CommandHandler<TestEntity, TestRequest> {

    @Override
    public TestEntity get(QueryArgs args) {
        return new TestEntity();
    }

    @Override
    public void accept(TestRequest testRequest) {
        // Emit TestEvent
        new TestEvent().setPayload(get(null)).emit();
    }

    @Override
    public Consumer<TestRequest> andThen(Consumer<? super TestRequest> after) {
        return null;
    }
}