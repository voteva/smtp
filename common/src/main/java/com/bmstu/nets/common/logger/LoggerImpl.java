package com.bmstu.nets.common.logger;

import com.bmstu.nets.common.config.PropertiesConfiguration;
import com.bmstu.nets.common.queue.LogQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.bmstu.nets.common.logger.LogLevel.DEBUG;
import static com.bmstu.nets.common.logger.LogLevel.ERROR;
import static com.bmstu.nets.common.logger.LogLevel.INFO;
import static com.bmstu.nets.common.logger.LogLevel.WARN;

public class LoggerImpl
        implements Logger {

    private final LoggerProperties properties;
    private final LogQueue logQueue;
    private final Class clazz;

    LoggerImpl(@Nonnull Class clazz) {
        this.properties = PropertiesConfiguration.instance().loadProperties(LoggerProperties.class);
        this.logQueue = LogQueue.instance();
        this.clazz = clazz;
    }

    @Override
    public void error(String message, Object... values) {
        if (isErrorEnabled()) {
            logQueue.enqueue(prepareLogMessage(ERROR, message, values));
        }
    }

    @Override
    public void warn(String message, Object... values) {
        if (isWarnEnabled()) {
            logQueue.enqueue(prepareLogMessage(WARN, message, values));
        }
    }

    @Override
    public void info(String message, Object... values) {
        if (isInfoEnabled()) {
            logQueue.enqueue(prepareLogMessage(INFO, message, values));
        }
    }

    @Override
    public void debug(String message, Object... values) {
        if (isDebugEnabled()) {
            logQueue.enqueue(prepareLogMessage(DEBUG, message, values));
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return properties.getLevel().getOrder() >= ERROR.getOrder();
    }

    @Override
    public boolean isWarnEnabled() {
        return properties.getLevel().getOrder() >= WARN.getOrder();
    }

    @Override
    public boolean isInfoEnabled() {
        return properties.getLevel().getOrder() >= INFO.getOrder();
    }

    @Override
    public boolean isDebugEnabled() {
        return properties.getLevel().getOrder() >= DEBUG.getOrder();
    }

    @Nonnull
    private String prepareLogMessage(@Nonnull LogLevel logLevel, @Nonnull String message, @Nullable Object... values) {
        String messageText = clazz.getSimpleName() + ": " + logLevel + " " + message;
        if (values != null && values.length > 0) {
            messageText = messageText.replaceAll("\\{}", "%s");
            return String.format(messageText, values);
        }
        return messageText;
    }
}
