package com.bmstu.nets.common.logger;

import lombok.Getter;

enum LogLevel {
    ERROR(0),
    WARN(1),
    INFO(2),
    DEBUG(3);

    @Getter
    private int order;

    LogLevel(int order) {
        this.order = order;
    }
}
