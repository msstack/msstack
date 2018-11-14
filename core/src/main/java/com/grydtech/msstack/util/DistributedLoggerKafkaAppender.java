package com.grydtech.msstack.util;

import com.grydtech.msstack.core.types.logging.LogMessage;

public class DistributedLoggerKafkaAppender extends DistributedLogger {
    public DistributedLoggerKafkaAppender(String className) {
        super(className);
    }

    @Override
    public void info(String message) {
        LogMessage logMessage = new LogMessage("INFO", message, getClassName());
        sendLogMessage(logMessage);
    }

    @Override
    public void warn(String message) {
        LogMessage logMessage = new LogMessage("WARN", message, getClassName());
        sendLogMessage(logMessage);
    }

    @Override
    public void error(String message) {
        LogMessage logMessage = new LogMessage("ERROR", message, getClassName());
        sendLogMessage(logMessage);
    }
}
