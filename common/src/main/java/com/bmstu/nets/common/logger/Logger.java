package com.bmstu.nets.common.logger;

public interface Logger {
    void error(String message, Object... values);
    void warn(String message, Object... values);
    void info(String message, Object... values);
    void debug(String message, Object... values);
    boolean isErrorEnabled();
    boolean isWarnEnabled();
    boolean isInfoEnabled();
    boolean isDebugEnabled();
}
