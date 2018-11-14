package com.grydtech.msstack.util;

import com.grydtech.msstack.core.connectors.messagebus.MessageBusConnector;
import com.grydtech.msstack.core.types.logging.LogMessage;

public abstract class DistributedLogger {
    private final String className;

    public DistributedLogger(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public abstract void info(String message);

    public abstract void warn(String message);

    public abstract void error(String message);

    protected final void sendLogMessage(LogMessage logMessage) {
        MessageBusConnector.getInstance().push(logMessage);
    }
}
