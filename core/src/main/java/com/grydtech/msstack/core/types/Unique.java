package com.grydtech.msstack.core.types;

public interface Unique<I> {

    I getId();

    Unique<I> setId(I id);
}
