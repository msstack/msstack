package com.grydtech.msstack.core.services;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.core.types.Entity;
import com.grydtech.msstack.core.types.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandlerWrapper {
    private String key;
    private Class<? extends Handler> handlerClass;
    private Class<? extends Message> messageClass;
    private Class<? extends Entity> entityClass;
}
