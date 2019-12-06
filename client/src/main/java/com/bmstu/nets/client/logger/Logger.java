package com.bmstu.nets.client.logger;

public interface Logger {
    void error();
    void warn();
    void info();
    void debug();
    boolean isErrorEnabled();
    boolean isWarnEnabled();
    boolean isInfoEnabled();
    boolean isDebugEnabled();
}
