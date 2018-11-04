package com.grydtech.msstack.core.types.messaging;

import java.util.UUID;

interface Request<P, M> extends Message<UUID, P>, Metadata<M> {
}
