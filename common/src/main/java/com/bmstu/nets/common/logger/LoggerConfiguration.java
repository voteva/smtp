package com.bmstu.nets.common.logger;

import lombok.Data;

import static com.bmstu.nets.common.logger.LogLevel.DEBUG;

@Data
public class LoggerConfiguration {
    private LogLevel level = DEBUG; // TODO read from file

    private static final class LoggerConfigurationHolder {
        static final LoggerConfiguration INSTANCE = new LoggerConfiguration();
    }

    private LoggerConfiguration() { }

    public static LoggerConfiguration instance() {
        return LoggerConfigurationHolder.INSTANCE;
    }
}
