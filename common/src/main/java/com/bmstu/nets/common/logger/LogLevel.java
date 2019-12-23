package com.bmstu.nets.common.logger;

import lombok.Getter;

public enum LogLevel {
    ERROR(0),
    WARN(1),
    INFO(3),
    DEBUG(4);

    @Getter
    private int order;

    LogLevel(int order) {
        this.order = order;
    }
}
