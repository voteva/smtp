package com.bmstu.nets.common.logger;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
class LoggerProperties {

    @SerializedName("logger.level")
    private LogLevel level = LogLevel.INFO;
}
