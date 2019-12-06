package com.bmstu.nets.client.logger;

public interface Logger {
    void error(String message, String... values);
    void warn(String message, String... values);
    void info(String message, String... values);
    void debug(String message, String... values);
    boolean isErrorEnabled();
    boolean isWarnEnabled();
    boolean isInfoEnabled();
    boolean isDebugEnabled();
}
