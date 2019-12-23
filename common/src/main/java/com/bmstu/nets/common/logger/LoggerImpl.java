package com.bmstu.nets.common.logger;

import com.bmstu.nets.common.queue.LogQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.bmstu.nets.common.logger.LogLevel.*;
import static com.bmstu.nets.common.logger.LogLevel.DEBUG;
import static com.bmstu.nets.common.logger.LogLevel.ERROR;
import static com.bmstu.nets.common.logger.LogLevel.INFO;
import static com.bmstu.nets.common.logger.LogLevel.WARN;

public class LoggerImpl
        implements Logger {

    private final LoggerConfiguration loggerConfiguration;
    private final LogQueue logQueue;
    private final Class clazz;

    LoggerImpl(Class clazz) {
        this.loggerConfiguration = LoggerConfiguration.instance();
        this.logQueue = LogQueue.instance();
        this.clazz = clazz;
    }

    @Override
    public void error(String message, String... values) {
        if (isErrorEnabled()) {
            logQueue.enqueue(prepareLogMessage(ERROR, message, values));
        }
    }

    @Override
    public void warn(String message, String... values) {
        if (isWarnEnabled()) {
            logQueue.enqueue(prepareLogMessage(WARN, message, values));
        }
    }

    @Override
    public void info(String message, String... values) {
        if (isInfoEnabled()) {
            logQueue.enqueue(prepareLogMessage(INFO, message, values));
        }
    }

    @Override
    public void debug(String message, String... values) {
        if (isDebugEnabled()) {
            logQueue.enqueue(prepareLogMessage(DEBUG, message, values));
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return loggerConfiguration.getLevel().getOrder() <= ERROR.getOrder();
    }

    @Override
    public boolean isWarnEnabled() {
        return loggerConfiguration.getLevel().getOrder() <= WARN.getOrder();
    }

    @Override
    public boolean isInfoEnabled() {
        return loggerConfiguration.getLevel().getOrder() <= INFO.getOrder();
    }

    @Override
    public boolean isDebugEnabled() {
        return loggerConfiguration.getLevel().getOrder() <= DEBUG.getOrder();
    }

    @Nonnull
    private String prepareLogMessage(@Nonnull LogLevel logLevel, @Nonnull String message, @Nullable String... values) {
        return clazz.getSimpleName() + ": " + logLevel + " " + message; // TODO
    }
}
