package com.grydtech.msstack.core.types;

public interface Entity<I> extends Unique<I> {

    @Override
    I getId();

    @Override
    Entity<I> setId(I id);
}
