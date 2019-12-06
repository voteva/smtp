package com.bmstu.nets.client.logger;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoggerImpl
        implements Logger {

    private final Class clazz;

    @Override
    public void error() {
        if (isErrorEnabled()) {

        }
    }

    @Override
    public void warn() {

    }

    @Override
    public void info() {

    }

    @Override
    public void debug() {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }
}
