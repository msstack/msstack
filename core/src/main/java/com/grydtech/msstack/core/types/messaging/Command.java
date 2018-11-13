package com.grydtech.msstack.core.types.messaging;

import com.grydtech.msstack.core.types.Entity;

/**
 * Command Class
 */
public abstract class Command<E extends Entity> extends Message<E> {
}
